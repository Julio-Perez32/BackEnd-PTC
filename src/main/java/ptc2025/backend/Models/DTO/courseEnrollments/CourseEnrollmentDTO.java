package ptc2025.backend.Models.DTO.courseEnrollments;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollmentDTO {

    private String id;

    @NotBlank(message = "El ID de la oferta del curso es obligatorio")
    private String courseOfferingId;

    @NotBlank(message = "El ID de la inscripción en la carrera es obligatorio")
    private String studentCareerEnrollmentId;

    @NotBlank(message = "El estado de la inscripción es obligatorio")
    private String enrollmentStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "La calificación máxima es 100")
    private Double finalGrade;

    @NotNull(message = "La fecha de inscripción no puede ser nula")
    private LocalDate enrollmentDate;

    private Double meritUnit;
}
