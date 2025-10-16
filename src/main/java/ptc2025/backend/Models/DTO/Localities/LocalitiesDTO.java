package ptc2025.backend.Models.DTO.Localities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LocalitiesDTO {
    private String localityID;
    @NotBlank
    private String universityID;
    private Boolean isMainLocality;
    @NotBlank(message = "La direccion no puede estar vacia")
    @Size(max = 500, message = "La direccion no puede ser mayor de 500 caracteres")
    private String address;
    @NotBlank(message = "Ingrese un número de teléfono")
    @Pattern(regexp = "\\d{4}-\\d{4}", message = "El formato debe ser: 1234-5678")
    private String phoneNumber;

    private String universityName;
}
/** LOCALITYID
 UNIVERSITYID
 ISMAINLOCALITY
 ADDRESS
 PHONENUMBER*/