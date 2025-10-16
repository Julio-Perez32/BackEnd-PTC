package ptc2025.backend.Models.DTO.Pensum;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PensumDTO {

    private String PensumID;

    @NotBlank(message = "El ID de la carrera es obligatorio")
    private String CareerID;

    @NotBlank(message = "La versión es obligatoria")
    @Size(max = 20, message = "La versión no puede exceder 20 caracteres")
    private String Version;

    // ✅ CORRECCIÓN: Cambiar @NotBlank por @NotNull (es un Long, no String)
    @NotNull(message = "El año efectivo es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    @Max(value = 2100, message = "El año debe ser menor o igual a 2100")
    private Long EffectiveYear;

    // ✅ Campo para mostrar el nombre de la carrera en el frontend
    private String career;
}