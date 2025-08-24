package ptc2025.backend.Models.DTO.AcademicLevel;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class AcademicLevelsDTO {

    private String academicLevelID;
    @NotBlank
    private String universityID;
    @NotBlank
    private String academicLevelName;

    private  String universityName;
}
