package ptc2025.backend.Models.DTO.EventTypes;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class eventTypesDTO {
    private String eventTypeID;
    @NotBlank (message = "Ingrese el id de la Universidad")
    private String universityID;
    @NotBlank (message = "Ingrese el tipo de evento")
    private String typeName;

}
