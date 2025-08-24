package ptc2025.backend.Models.DTO.employees;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeeDTO {

    private String id;

    @NotBlank(message = "La persona no puede quedar vacia")
    private String personID;

    @NotBlank(message = "El departamente al que pertenece el empleado no puede quedar vacio")
    private String deparmentID;

    @NotBlank(message = "El codigo del empleado debe de estar completo")
    private String employeeCode;

    @NotBlank(message = "Ingrese los detalles sobre el empleado")
    private String EmployeeDetail;

    //campo para la persona
    private String personName;
    private String personLastName;

}
