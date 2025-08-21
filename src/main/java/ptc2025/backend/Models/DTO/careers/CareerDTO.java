package ptc2025.backend.Models.DTO.careers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CareerDTO {

    private String id;

    @NotNull(message = "El nombre de la carrera no puede ser nulo")
    @NotBlank(message = "El nombre de la carrera no puede estar vacío")
    private String name;

    private String description;

    @NotNull(message = "La facultad asociada no puede ser nula")
    @NotBlank(message = "La facultad asociada no puede estar vacía")
    private String facultyId;

    @NotNull(message = "El estado de actividad es obligatorio")
    private Boolean isActive = true;
}
