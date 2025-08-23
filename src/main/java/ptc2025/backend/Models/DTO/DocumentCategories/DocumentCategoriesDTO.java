package ptc2025.backend.Models.DTO.DocumentCategories;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentCategoriesDTO {
    private String id;
    @NotBlank
    private String universityID;
    @NotBlank
    private String documentCategory;
}
