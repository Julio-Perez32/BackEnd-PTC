package ptc2025.backend.Models.DTO.rolePermissions;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class rolePermissionsDTO {
    private String id;
    @NotBlank
    private String permissionID;
    @NotBlank
    private String roleID;
}
