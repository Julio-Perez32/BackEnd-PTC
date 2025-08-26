package ptc2025.backend.Models.DTO.careerCycleAvailability;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerCycleAvailabilityDTO {

    private String id;

    @NotBlank(message = "El ciclo académico es obligatorio")
    private String yearCycleId;

    @NotBlank(message = "El ID de la carrera es obligatorio")
    private String careerId;
}
