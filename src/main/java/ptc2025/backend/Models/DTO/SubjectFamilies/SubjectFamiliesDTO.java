package ptc2025.backend.Models.DTO.SubjectFamilies;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @EqualsAndHashCode
public class SubjectFamiliesDTO {

    private String subjectFamilyID;
    @NotBlank
    private String facultyID;
    @NotBlank @Size(max = 10)
    private String subjectPrefix;
    @Size(max = 3)
    private Long reservedSlots;
    @NotBlank @Size(max = 5)
    private Long startingNumber;
    @Size(max = 5)
    private Long lastAssignedNumber;


    private String facultyName;
}
