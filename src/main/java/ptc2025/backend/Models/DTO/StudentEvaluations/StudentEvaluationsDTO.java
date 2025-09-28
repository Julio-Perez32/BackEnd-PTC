package ptc2025.backend.Models.DTO.StudentEvaluations;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString @EqualsAndHashCode
public class StudentEvaluationsDTO {
    private String id;
    private String componentID;
    private String courseEnrollmentID;
    @NotBlank
    private int score;
    @NotBlank
    private String feedback;
    @NotBlank
    private LocalDate submittedAt;

    private String component;
    private String courseEnrollment;
    private String user;
    private String planComponents;
}
