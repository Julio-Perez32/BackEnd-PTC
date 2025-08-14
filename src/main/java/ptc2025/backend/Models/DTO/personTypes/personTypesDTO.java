package ptc2025.backend.Models.DTO.personTypes;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class personTypesDTO {
    private String personTypeID;
    @NotBlank(message = "El campo de tipo de persona no puede quedar vacio")
    private String personType;
}
