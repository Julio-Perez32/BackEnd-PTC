package ptc2025.backend.Models.DTO.Students;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString @EqualsAndHashCode @Getter @Setter
public class StudentsDTO {

    private String studentID;
    @NotBlank
    private String personID;
    @NotBlank
    private String studentCode;

    private String personName;
    private String personLastName;


    // Nuevos campos para cascada
    private String careerId;        // carrera en la que se inscribe
    private String yearCycleId;     // ciclo académico
    private String careerEnrollmentID;
    private String cycleEnrollmentID;
    private String performanceID;
}
