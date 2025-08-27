package ptc2025.backend.Services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Models.DTO.Auth.LoginResponseDTO;
import ptc2025.backend.Respositories.Users.UsersRespository;

@Service
public class AuthService {
    @Autowired
    UsersRespository UsersRespository;

    public LoginResponseDTO login(String email, String password) {
        return UsersRespository.findByEmail(email)
                .map(user -> {
                    if (!user.getContrasena().equals(password)) {
                        return new LoginResponseDTO(
                                false,
                                "Contraseña incorrecta",
                                user.getId(),
                                user.getEmail(),
                                user.getSystemRoles() != null ? user.getSystemRoles().getRoleName() : null,
                                user.getPeople() != null ? user.getPeople().getFirstName() : null
                        );
                    }

                    return new LoginResponseDTO(
                            true,
                            "Inicio de sesión exitoso",
                            user.getId(),
                            user.getEmail(),
                            user.getSystemRoles() != null ? user.getSystemRoles().getRoleName() : null,
                            user.getPeople() != null ? user.getPeople().getFirstName() : null
                    );
                })
                .orElse(new LoginResponseDTO(
                        false,
                        "Usuario no encontrado",
                        null,
                        null,
                        null,
                        null
                ));
    }

}
