package ptc2025.backend.Models.DTO.careerCycleAvailability;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerCycleAvailabilityDTO {

    private String id;

    @NotBlank(message = "El ID de la carrera es obligatorio")
    private String careerId;

    @NotBlank(message = "El año académico es obligatorio")
    private String academicYearId;

    @NotBlank(message = "El código de ciclo es obligatorio")
    private String cycleCode;

    private Integer maxCapacity;

    private Boolean isActive = true;
}
