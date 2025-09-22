package ptc2025.backend.Controller.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.UserProfile.UserProfileDTO;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Services.Auth.AuthService;
import ptc2025.backend.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
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
    private ResponseEntity<String> login(@Valid @RequestBody UsersDTO dto, HttpServletResponse response){
        if(dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getContrasena() == null || dto.getContrasena().isBlank()){
            return ResponseEntity.status(401).body("Error: Las credenciales no estan completas");
        }
        addTokenCookie(response, dto.getEmail());
        if (service.Login(dto.getEmail(), dto.getContrasena())){
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");

    }

    private void addTokenCookie(HttpServletResponse response, String email) {
        Optional<UsersEntity> usersOpt = service.getUser(email);
        if(usersOpt.isPresent()){
            UsersEntity user = usersOpt.get();
            String token = jwtUtils.create(
              String.valueOf(user.getId()),
              user.getEmail(),
              user.getSystemRoles().getRoleName()
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("No hay token en la cookie");
        }

        try {
            // validacion de la firma del token
            if (!jwtUtils.validate(token)) {
                return ResponseEntity.status(401).body("Token inválido");
            }

            // verificacion si está expirado
            Claims claims = jwtUtils.parseToken(token); // parseToken lanza ExpiredJwtException si ya expiró
            if (claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
                return ResponseEntity.status(401).body("Token expirado");
            }

            // Extrae el email del subject
            String email = jwtUtils.getValue(token);

            Optional<UserProfileDTO> userProfile = service.getUserProfile(email);
            if (userProfile.isPresent()) {
                return ResponseEntity.ok(userProfile.get());
            } else {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Token expirado");
        } catch (JwtException e) {
            return ResponseEntity.status(401).body("Token inválido");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }




}
