package ptc2025.backend.Models.DTO.courseEnrollments;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String id;

    @NotNull(message = "El ID de la oferta del curso es obligatorio")
    private String courseOfferingId;

    @NotNull(message = "El ID de la inscripción en la carrera es obligatorio")
    private String studentCareerEnrollmentId;

    @NotBlank(message = "El estado de la inscripción es obligatorio")
    private String enrollmentStatus;

    @DecimalMin(value = "0.0", inclusive = true, message = "La calificación mínima es 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "La calificación máxima es 100")
    private Double finalGrade;

    @NotNull(message = "La fecha de inscripción no puede ser nula")
    private LocalDate enrollmentDate;

    private Double meritUnit;

    // Derivados (igual que en CareerDTO agregaron nombres relacionados)
    private String courseOfferingName;
    private String studentName;
    private String careerName;
}
