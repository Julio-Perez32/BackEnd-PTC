package ptc2025.backend.Models.DTO.documentCategories;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class documentCategoriesDTO {
    @NotNull
    private String id;
    @NotNull
    private String universityID;
    @NotNull
    private String documentCategory;
}
