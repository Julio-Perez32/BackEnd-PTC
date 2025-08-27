package ptc2025.backend.Models.DTO.studentCycleEnrollments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCycleEnrollmentDTO {

    private String id;

    private String studentCareerEnrollmentId;

    private String yearCycleID;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    private LocalDate registeredAt;
    private LocalDate completedAt;

    private String studentcareerenrollment;
    private String yearcycle;

}
