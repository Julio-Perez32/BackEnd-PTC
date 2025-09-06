package ptc2025.backend.Controller.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Services.Auth.AuthService;
import ptc2025.backend.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

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


}
