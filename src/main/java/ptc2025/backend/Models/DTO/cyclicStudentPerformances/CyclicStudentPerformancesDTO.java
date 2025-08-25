package ptc2025.backend.Models.DTO.CyclicStudentPerformances;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @EqualsAndHashCode @ToString
public class CyclicStudentPerformancesDTO {

    private String performanceID;

    @NotBlank(message = "El studentCycleEnrollmentID es obligatorio")
    private String studentCycleEnrollmentID;

    private Integer totalValueUnits;
    private Integer totalMeritUnit;
    private Double meritUnitCoefficient;
    private LocalDate computedAt;

    //Derivados
    private String studentID;
    private String studentName;
    private String careerID;
    private String careerName;
}
