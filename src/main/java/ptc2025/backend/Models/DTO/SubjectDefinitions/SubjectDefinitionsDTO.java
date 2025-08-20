package ptc2025.backend.Models.DTO.SubjectDefinitions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString @NotBlank
public class SubjectDefinitionsDTO {

    private String SubjectID;
    private String SubjectFamilyID;
    @Size(max = 100)
    private String SubjectName;
    @Size(max = 10)
    private String SubjectCode;
}
