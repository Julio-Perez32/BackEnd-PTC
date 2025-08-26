package ptc2025.backend.Models.DTO.FacultyDeans;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString @EqualsAndHashCode
public class FacultyDeansDTO {
    private String id;
    @NotBlank
    private String facultyID;
    @NotBlank
    private String employeeID;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
}
