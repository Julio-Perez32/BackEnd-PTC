package ptc2025.backend.Models.DTO.CourseOfferingsTeachers;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class CourseOfferingsTeachersDTO {

    private String CourseOfferingTeacherID;
    @NotBlank
    private String CourseOfferingID;
    @NotBlank
    private String EmployeeID;

    private String coureOffering;
    private String employee;
}
