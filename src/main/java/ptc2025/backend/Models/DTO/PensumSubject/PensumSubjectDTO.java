package ptc2025.backend.Models.DTO.PensumSubject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class PensumSubjectDTO {

    @NotBlank
    private String PensumSubjectID;
    @NotBlank
    private String PensumID;
    @NotBlank
    private String SubjectID;
    @NotNull
    private Long ValueUnits;
    private Character IsRequired;

    private String pensum;
    private String subject;
}
