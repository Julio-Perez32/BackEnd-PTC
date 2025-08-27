package ptc2025.backend.Models.DTO.careerCycleAvailability;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerCycleAvailabilityDTO {

    private String id;

    private String yearCycleId;

    private String careerId;

    private String career;
    private String yearCycle;
}
