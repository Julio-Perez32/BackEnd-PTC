package ptc2025.backend.Models.DTO.facultyCorrelatives;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class facultyCorrelativesDTO {
    private String correlativeID;

    @NotBlank(message = "El campo de la facultad no puede estar vacio")
    private String facultyID;

    @NotBlank(message = "El correlativo no puede estar vacio")
    private Integer correlativeNumber;

    private String facultyName;
}
