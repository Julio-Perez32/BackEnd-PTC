package ptc2025.backend.Models.DTO.FacultyLocalities;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class FacultyLocalitiesDTO {
    private String id;

    private String facultyID;

    private String localityID;

    private String faculties;
    private String localities;
}
