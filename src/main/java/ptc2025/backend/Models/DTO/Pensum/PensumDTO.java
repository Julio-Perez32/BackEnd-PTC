package ptc2025.backend.Models.DTO.Pensum;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class PensumDTO {

    private String PensumID;
    @NotBlank
    private String CareerID;
    @NotBlank @Size(max = 20)
    private String Version;@NotNull(message = "El año efectivo es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2100, message = "El año debe ser menor a 2100")

    private Long EffectiveYear;


    private String career;
}
