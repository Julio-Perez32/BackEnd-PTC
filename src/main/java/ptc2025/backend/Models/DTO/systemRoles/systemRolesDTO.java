package ptc2025.backend.Models.DTO.systemRoles;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class systemRolesDTO {
    private String roleID;
    @NotBlank(message = "El nombre del rol no puede estar vacio")
    private String roleName;


}
