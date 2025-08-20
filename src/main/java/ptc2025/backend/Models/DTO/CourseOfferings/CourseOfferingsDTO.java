package ptc2025.backend.Models.DTO.CourseOfferings;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class CourseOfferingsDTO {

    private String CourseOfferingID;
    @NotBlank
    private String SubjectID;
    @NotBlank
    private String YearCycleID;
}
