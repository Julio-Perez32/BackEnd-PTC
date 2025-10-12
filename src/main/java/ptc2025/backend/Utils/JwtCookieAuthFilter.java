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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

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

            // Valida y parsea
            Claims claims = jwtUtils.parseToken(token);

            // ⚠️ El rol viene en el claim "rol" según tu JWTUtils.create(...)
            String rol = jwtUtils.extractRol(token); // p.ej.: "Docente", "Administrador", "Registro Académico"
            if (rol == null || rol.isBlank()) {
                log.warn("⚠️ Token sin rol válido");
                sendError(response, "Rol no válido en token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // ❗ NO normalizar (no reemplazar espacios/acentos). Solo trim.
            rol = rol.trim();

            // ✅ Prefija con ROLE_ para que matchee hasAnyRole(...)
            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), // correo (subject)
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Log de verificación
            log.info("🔐 Authorities en contexto: {}", authentication.getAuthorities());

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

    // ===== helpers =====

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String p = (request.getRequestURI() == null ? "" : request.getRequestURI().trim().toLowerCase());
        String m = (request.getMethod() == null ? "" : request.getMethod().trim().toUpperCase());

        boolean isLogin     = (p.equals("/api/auth/login")  || p.equals("/api/auth/login/"))  && (m.equals("POST") || m.equals("OPTIONS"));
        boolean isLogout    = (p.equals("/api/auth/logout") || p.equals("/api/auth/logout/")) && (m.equals("POST") || m.equals("OPTIONS"));
        boolean isPreflight = m.equals("OPTIONS"); // CORS preflight

        return isLogin || isLogout || isPreflight;
    }

    private String resolveToken(HttpServletRequest request) {
        String cookieToken = extractTokenFromCookies(request);
        if (cookieToken != null && !cookieToken.isBlank()) return cookieToken;

        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
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
        response.getWriter().write(String.format("{\"error\":\"%s\",\"status\":%d}", message, status));
    }
}
