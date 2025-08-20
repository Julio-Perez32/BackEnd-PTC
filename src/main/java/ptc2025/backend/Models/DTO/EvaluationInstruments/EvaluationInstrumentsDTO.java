package ptc2025.backend.Models.DTO.EvaluationInstruments;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class EvaluationInstrumentsDTO {

    private String instrumentID;
    @NotBlank
    private String instrumentTypeID;
    private String description;
    private Character usesRubric;
}
