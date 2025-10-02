package ptc2025.backend.Controller.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.UserProfile.UserProfileDTO;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Services.Auth.AuthService;
import ptc2025.backend.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(
        origins = {
                "https://localhost", "http://localhost:5173",
                "capacitor://localhost", "ionic://localhost",
                "https://sapientiae-web.vercel.app"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/Auth")
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid @RequestBody UsersDTO dto, HttpServletResponse response) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getContrasena() == null || dto.getContrasena().isBlank()) {
            return ResponseEntity.status(401).body(Map.of("status","error","message","Las credenciales no están completas"));
        }

        if (!service.Login(dto.getEmail(), dto.getContrasena())) {
            return ResponseEntity.status(401).body(Map.of("status","error","message","Credenciales incorrectas"));
        }

        // genera token
        UsersEntity user = service.getUser(dto.getEmail()).orElseThrow();
        String token = jwtUtils.create(String.valueOf(user.getId()), user.getEmail(), user.getSystemRoles().getRoleName());

        // cookie para web
        ResponseCookie cookie = ResponseCookie.from("authToken", token)
                .httpOnly(true).secure(true).sameSite("None").path("/").maxAge(60 * 60 * 24).build();
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        // también devolver en el body para móvil
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "token", token
        ));
    }


    private void addTokenCookie(HttpServletResponse response, String email) {
        Optional<UsersEntity> usersOpt = service.getUser(email);
        if (usersOpt.isPresent()) {
            System.out.println("hola si es verdadero");
            UsersEntity user = usersOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getId()),
                    user.getEmail(),
                    user.getSystemRoles().getRoleName()
            );

            ResponseCookie cookie = ResponseCookie.from("authToken", token) // ← mismo nombre que @CookieValue
                    .httpOnly(true)           // que no sea accesible por JS
                    .secure(true)             // OBLIGATORIO en cross-site (y Heroku va por HTTPS)
                    .sameSite("None")         // OBLIGATORIO para que viaje desde Vercel → Heroku
                    .path("/")                // que aplique a todo tu /api
                    //.domain("sapientiae-api-bd9a54b3d7a1.herokuapp.com") // NORMALMENTE OMITE ESTO
                    .maxAge(60 * 60 * 24)     // opcional: 1 día
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sesión cerrada y cookie eliminada"
        ));
    }

    private String resolveToken(HttpServletRequest request, String cookieToken) {
        if (cookieToken != null && !cookieToken.isBlank()) return cookieToken;
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(
            @CookieValue(value = "authToken", required = false) String cookieToken,
            HttpServletRequest request) {

        String token = resolveToken(request, cookieToken);
        if (token == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "status","error",
                    "message","No hay token"
            ));
        }

        try {
            if (!jwtUtils.validate(token)) {
                return ResponseEntity.status(401).body(Map.of(
                        "status","error",
                        "message","Token inválido"
                ));
            }

            Claims claims = jwtUtils.parseToken(token);
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                return ResponseEntity.status(401).body(Map.of(
                        "status","error",
                        "message","Token expirado"
                ));
            }

            String email = jwtUtils.getValue(token);
            Optional<UserProfileDTO> userProfile = service.getUserProfile(email);
            return userProfile
                    .<ResponseEntity<?>>map(up -> ResponseEntity.ok(Map.of(
                            "status","success",
                            "user", up
                    )))
                    .orElseGet(() -> ResponseEntity.status(404).body(Map.of(
                            "status","error",
                            "message","Usuario no encontrado"
                    )));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status","error",
                    "message","Token expirado"
            ));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status","error",
                    "message","Token inválido"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status","error",
                    "message","Error interno del servidor"

            ));
        }
    }
}
