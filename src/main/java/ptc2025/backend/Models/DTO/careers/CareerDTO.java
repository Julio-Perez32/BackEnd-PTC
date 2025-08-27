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

    @NotNull(message = "El nivel académico es obligatorio")
    private String academicLevelId;

    @NotNull(message = "El tipo de grado es obligatorio")
    private String degreeTypeId;

    @NotNull(message = "La modalidad es obligatoria")
    private String modalityId;

    @NotNull(message = "El departamento es obligatorio")
    private String departmentId;

    @NotBlank(message = "El nombre de la carrera no puede estar vacío")
    private String name;

    @NotBlank(message = "Ingrese el codigo de la carrera")
    private String careerCode;
    @NotBlank(message = "Ingrese la descripcion de la carrera")
    private String description;
    @NotBlank(message = "Ingrese la nota minima para la carrera")
    private Integer minPassingScore;
    @NotBlank(message = "Ingrese el MUC minimo para la carrera")
    private Integer minMUC;
    @NotBlank(message = "Ingrese las materias obligatorias de la carrera")
    private Integer compulsorySubjects;
    @NotBlank(message = "Ingrese el total de unidades valorativas de la carrera")
    private Integer totalValueUnits;

    private String academicLevelName;
    private String modalityName;
    private String degreeTypeName;
    private String departmentName;

}
