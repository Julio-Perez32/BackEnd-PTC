package ptc2025.backend.Models.DTO.cyclicStudentPerformances;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CyclicStudentPerformanceDTO {

    private String performanceID;

    private String studentCycleEnrollmentID;

    private Integer totalValueUnits;
    private Integer totalMeritUnit;
    private Double meritUnitCoefficient;
    private LocalDate computedAt;

    // Derivados
    private String studentID;
    private String studentName;
    private String careerID;
    private String careerName;

    private String StudentCycleEnrollment;
}
