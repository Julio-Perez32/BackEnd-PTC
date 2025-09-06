package ptc2025.backend.Models.DTO.Users;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UsersDTO {

    private String id;

    private String universityID;
    private String personId;
    private String roleId;
    @NotNull @Email
    private String email;

    @NotNull @Size(min = 8, message = "La contraseña debe de tener 8 caracteres")
    private String contrasena;


    private String rolesName;
    private String universityName;

    private String personName;
    private String personLastName;

}