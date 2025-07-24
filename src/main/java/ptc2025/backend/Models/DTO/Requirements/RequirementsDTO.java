package ptc2025.backend.Models.DTO.Requirements;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequirementsDTO {
    private String id;
    @NotBlank
    private String universityID;
    @NotBlank
    private String requirementName;
    @NotBlank
    private String description;
}
