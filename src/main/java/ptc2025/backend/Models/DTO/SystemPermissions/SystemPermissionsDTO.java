package ptc2025.backend.Models.DTO.SystemPermissions;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SystemPermissionsDTO {
    private String id;
    @NotBlank
    private String categoryID;
    @NotBlank
    private String permissionName;
    @NotBlank
    private boolean managePermissions;
}
