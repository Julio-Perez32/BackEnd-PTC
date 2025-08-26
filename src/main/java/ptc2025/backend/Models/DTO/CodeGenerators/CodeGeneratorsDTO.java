package ptc2025.backend.Models.DTO.CodeGenerators;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CodeGeneratorsDTO {
    private String generatorID;
    private String entityTypeID;
    private String correlativeID;

    @NotBlank(message = "El prefijo no puede estar vacio, por favor ingresarlo")
    private  String prefix;

    @NotBlank(message = "Ingrese la longitud del sufijo")
    private Integer suffixLength;

    @NotBlank(message = "Ingrese el ultimo numero asignado para cada entidad")
    private Integer lastAssignedNumber;

    private String entityTypeName;

    private String facultyCorrelativesName;
    private String facultyCorrelativesID;
}
/**
 * generatorID
 * entityTypeID
 * correlativeID
 * prefix
 * suffixLength
 * lastAssignedNumber
 * */

