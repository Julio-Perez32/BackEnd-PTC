package ptc2025.backend.Models.DTO.systemPermissions;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class systemPermissionsDTO {
    private String id;
    @NotBlank
    private String categoryID;
    @NotBlank
    private String permissionName;
    @NotBlank
    private boolean managePermissions;
}
