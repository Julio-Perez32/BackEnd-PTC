package ptc2025.backend.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);
    private static final String AUTH_COOKIE_NAME = "authToken";
    private final JWTUtils jwtUtils;

    @Autowired
    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 🔓 Permitir endpoints públicos
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = resolveToken(request);

            if (token == null || token.isBlank()) {
                sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Claims claims = jwtUtils.parseToken(token);

            // 🎯 Extraer rol desde el token
            String rol = jwtUtils.extractRol(token);
            System.out.println("🎯 Rol extraído del token: '" + rol + "'");

            if (rol == null || rol.isBlank()) {
                log.warn("⚠️ Token sin rol válido, no se puede autenticar");
                sendError(response, "Rol no válido en token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 🔧 Normalizar para que siempre funcione
            rol = rol.replace("ROLE_", "").trim(); // quitar prefijo duplicado si lo tuviera
            if (!rol.isEmpty()) {
                rol = rol.substring(0, 1).toUpperCase() + rol.substring(1).toLowerCase(); // ej: "DOCENTE" → "Docente"
            }

            // Crear autoridad válida para Spring
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            // Crear autenticación y guardarla en el contexto
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), // usuario (correo)
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("⏰ Token expirado: {}", e.getMessage());
            sendError(response, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            log.warn("❌ Token malformado: {}", e.getMessage());
            sendError(response, "Token inválido", HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error("💥 Error de autenticación: {}", e.getMessage());
            sendError(response, "Error de autenticación", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format(
                "{\"error\": \"%s\", \"status\": %d}", message, status));
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints que no requieren token
        return (path.equals("/api/Auth/login") && "POST".equals(method)) ||
                (path.equals("/api/Auth/logout") && "POST".equals(method)) ||
                (path.startsWith("/api/Public/") && "GET".equals(method)) ||
                (path.equals("/api/Auth/register") && "POST".equals(method));
    }

    private String resolveToken(HttpServletRequest request) {
        // 1️⃣ Buscar en cookies
        String cookieToken = extractTokenFromCookies(request);
        if (cookieToken != null && !cookieToken.isBlank()) return cookieToken;

        // 2️⃣ Buscar en header Authorization: Bearer
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }
}
