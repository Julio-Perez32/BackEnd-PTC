package ptc2025.backend.Models.DTO.YearCycles;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @ToString @EqualsAndHashCode
public class YearCyclesDTO {
    private String id;


    @NotNull private String academicYearID;
    @NotNull private String cycleTypeID;


    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;


    private String academicyear;
    private String cycletype;
}
