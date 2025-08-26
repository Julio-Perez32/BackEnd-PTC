package ptc2025.backend.Models.DTO.SubjectTeachers;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString @NotBlank
public class SubjectTeachersDTO {

    private String SubjectTeacherID;
    private String SubjectID;
    private String EmployeeID;

    private String subject;
    private String employee;
}
