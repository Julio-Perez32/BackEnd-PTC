package ptc2025.backend.Models.DTO.StudentCareerEnrollments;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @EqualsAndHashCode @ToString
public class StudentCareerEnrollmentsDTO {

    private String studentCareerEnrollmentID;

    private String careerID;

    private String studentID;

    private String SocialServiceProjectID;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private LocalDate statusDate;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;
    private String serviceStatus;
    private LocalDate serviceStatusDate;

    // Campos derivados para respuesta
    private String careerName;
    private String studentName;
    private String SocialServiceProjectName;
}
