package ptc2025.backend.Models.DTO.EmployeeRoles;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRolesDTO {
    private String id;
    @NotNull
    private String universityID;
    @NotNull
    private String roleName;
    @NotNull
    private String roleType;
}
