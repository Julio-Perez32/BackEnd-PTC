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

    @NotNull(message = "El nombre completo no puede ser nulo")
    @NotBlank(message = "El nombre completo no puede estar vacío")
    private String fullName;

    @NotNull(message = "El correo electrónico no puede ser nulo")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Correo electrónico inválido")
    private String email;

    private String position;

    private String phone;

    @NotNull(message = "El estado de actividad es obligatorio")
    private Boolean isActive = true;
}
