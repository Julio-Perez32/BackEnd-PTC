package ptc2025.backend.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class UserDTO {

    @NotNull
    private String id;
    @NotNull
    private String universityID;
    @NotNull @Email
    private String email;
    @NotNull
    private String usuario;
    @NotNull @Size(min = 8, message = "La contraseña debe de ser de almenos por hay unos caracteres no jeje")
    private String contrasena;
    @NotNull @DateTimeFormat
    private Date fechaCreacion;

}
