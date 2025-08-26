package ptc2025.backend.Models.DTO.Departments;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode @Getter @Setter
public class DepartmentsDTO {

    private String departmentID;
    @NotBlank
    private String facultyID;
    @NotBlank
    private String departmentName;
    @NotBlank
    private String departmentType;

    private String facultyName;

}
