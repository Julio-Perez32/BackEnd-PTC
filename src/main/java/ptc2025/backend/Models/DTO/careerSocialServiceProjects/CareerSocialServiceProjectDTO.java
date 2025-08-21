package ptc2025.backend.Models.DTO.careerSocialServiceProjects;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CareerSocialServiceProjectDTO {

    private String id;

    @NotBlank(message = "La carrera es obligatoria")
    private String careerId;

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    private String projectName;

    private String supervisorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive = true;
}
