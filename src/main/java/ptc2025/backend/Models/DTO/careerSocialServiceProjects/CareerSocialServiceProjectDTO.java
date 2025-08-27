package ptc2025.backend.Models.DTO.careerSocialServiceProjects;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareerSocialServiceProjectDTO {

    private String id;

    @NotBlank(message = "La carrera es obligatoria")
    private String careerId;

    @NotBlank(message = "El proyecto de servicio social es obligatorio")
    private String socialServiceProjectId;

    private String careerName;

    private String socialServiceProjectName;
}
