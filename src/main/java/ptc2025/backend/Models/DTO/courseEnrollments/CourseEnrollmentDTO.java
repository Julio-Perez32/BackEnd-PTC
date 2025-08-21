package ptc2025.backend.Models.DTO.courseEnrollments;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CourseEnrollmentDTO {

    private String enrollmentId;

    @NotBlank(message = "El estudiante es obligatorio")
    private String studentId;

    @NotBlank(message = "La oferta del curso es obligatoria")
    private String offeringId;

    @NotNull(message = "La fecha de inscripción no puede ser nula")
    private LocalDate enrollmentDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "La calificación máxima es 100")
    private Double grade;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean isActive = true;
}
