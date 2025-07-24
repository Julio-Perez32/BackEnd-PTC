package ptc2025.backend.Models.DTO.degreeTypes;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DegreeTypesDTO {
    private String id;
    @NotBlank
    private String universityID;
    @NotBlank
    private String degreeTypeName;
}
