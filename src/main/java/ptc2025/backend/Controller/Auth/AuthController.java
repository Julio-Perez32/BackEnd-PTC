package ptc2025.backend.Controller.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/Auth")
public class AuthController {
    @Autowired
    private AuthService service;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<?> login(@Valid @RequestBody UsersDTO dto, HttpServletResponse response) {
        System.out.println(dto);

        if (dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getContrasena() == null || dto.getContrasena().isBlank()) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Las credenciales no están completas"
            ));
        }

        if (service.Login(dto.getEmail(), dto.getContrasena())) {
            addTokenCookie(response, dto.getEmail());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Inicio de sesión exitoso"
            ));
        }

        return ResponseEntity.status(401).body(Map.of(
                "status", "error",
                "message", "Credenciales incorrectas"
        ));
    }


    private void addTokenCookie(HttpServletResponse response, String email) {
        Optional<UsersEntity> usersOpt = service.getUser(email);
        if (usersOpt.isPresent()) {
            UsersEntity user = usersOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getId()),
                    user.getEmail(),
                    user.getSystemRoles().getRoleName()
            );

            String cookieValue = String.format(
                    "authToken=%s; "+
                            "Path=/;" +
                            "HttpOnly;"+
                            "Secure;"+
                            "SameSite=None;"+
                            "Max-Age=86400;"+
                            "Domain = sapientiae-api-bd9a54b3d7a1.herokuapp.com/",
                    token

            );

            response.addHeader("Set-Cookie", cookieValue);
            response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
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



    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "No hay token en la cookie"
            ));
        }

        try {
            // validación de la firma del token
            if (!jwtUtils.validate(token)) {
                return ResponseEntity.status(401).body(Map.of(
                        "status", "error",
                        "message", "Token inválido"
                ));
            }

            // verificación si está expirado
            Claims claims = jwtUtils.parseToken(token);
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                return ResponseEntity.status(401).body(Map.of(
                        "status", "error",
                        "message", "Token expirado"
                ));
            }

            // Extrae el email del subject
            String email = jwtUtils.getValue(token);

            Optional<UserProfileDTO> userProfile = service.getUserProfile(email);
            if (userProfile.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "user", userProfile.get()
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Usuario no encontrado"
                ));
            }

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Token expirado"
            ));
        } catch (JwtException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Token inválido"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }





}
