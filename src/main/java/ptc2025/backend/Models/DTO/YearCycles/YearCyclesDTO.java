package ptc2025.backend.Models.DTO.YearCycles;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString @EqualsAndHashCode
public class YearCyclesDTO {
    private String id;
    private String academicYearID;
    private String cycleTypeID;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
}
