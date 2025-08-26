package ptc2025.backend.Models.DTO.Faculties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class FacultiesDTO {

    private String facultyID;
    private String FacultyName;
    @NotBlank
    private String facultyCode;
    @Pattern(regexp = "^\\+503\\s\\d{4}-\\d{4}$", message = "El número debe tener el formato: +503 1234-5678")
    private String contactPhone;
    private String correlativeCode;
}
