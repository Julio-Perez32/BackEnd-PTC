package ptc2025.backend.Controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Models.DTO.Auth.LoginRequestDTO;
import ptc2025.backend.Models.DTO.Auth.LoginResponseDTO;
import ptc2025.backend.Services.AuthService.AuthService;

@RestController
@RequestMapping("/api/Auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            return service.login(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception e) {
            return new LoginResponseDTO(
                    false,
                    "Error en el servidor: " + e.getMessage(),
                    null,
                    null,
                    null,
                    null
            );
        }
    }

}
