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

    @NotBlank(message = "El ciclo académico es obligatorio")
    private String yearCycleId;

    private String careerCode;
    private String description;
    private Integer minPassingScore;
    private Integer minMUC;
    private Integer compulsorySubjects;
    private Integer totalValueUnits;
}
