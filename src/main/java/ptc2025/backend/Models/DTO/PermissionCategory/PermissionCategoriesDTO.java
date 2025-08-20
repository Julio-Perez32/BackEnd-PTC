package ptc2025.backend.Models.DTO.PermissionCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class PermissionCategoriesDTO {

    private String categoryID;
    @NotBlank
    private String categoryName;
}
