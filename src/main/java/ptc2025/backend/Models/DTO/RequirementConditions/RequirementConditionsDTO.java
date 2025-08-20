package ptc2025.backend.Models.DTO.RequirementConditions;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString @NotBlank
public class RequirementConditionsDTO {

    private String ConditionID;
    private String RequirementID;
    private String SubjectID;
}
