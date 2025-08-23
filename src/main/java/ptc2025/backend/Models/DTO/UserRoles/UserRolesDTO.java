package ptc2025.backend.Models.DTO.UserRoles;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserRolesDTO {

    private String userRoleid;
    @NotBlank (message = "No puede dejar el usuario vacio")
    private String Userid;
    @NotBlank(message = "Por favor ingrese el tipo de rol de usuario")
    private String roleType;

}
