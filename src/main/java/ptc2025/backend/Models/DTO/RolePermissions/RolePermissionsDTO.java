package ptc2025.backend.Models.DTO.RolePermissions;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionsDTO {
    private String id;
    @NotBlank
    private String permissionID;
    @NotBlank
    private String roleID;
}
