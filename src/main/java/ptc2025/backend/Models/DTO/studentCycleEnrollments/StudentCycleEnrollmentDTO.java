package ptc2025.backend.Models.DTO.studentCycleEnrollments;

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
public class StudentCycleEnrollmentDTO {

    private String id;

    @NotNull(message = "El ID del estudiante es obligatorio")
    @NotBlank(message = "El ID del estudiante no puede estar vacío")
    private String studentId;

    @NotNull(message = "El ID del ciclo es obligatorio")
    @NotBlank(message = "El ID del ciclo no puede estar vacío")
    private String cycleId;

    private LocalDate enrollmentDate;
    private String status;

    @NotNull(message = "El estado de actividad es obligatorio")
    private Boolean isActive = true;
}
