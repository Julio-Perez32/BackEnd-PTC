package ptc2025.backend.Models.DTO.Universities;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter @Setter @ToString @EqualsAndHashCode
public class UniversityDTO{
    @NotBlank @Getter @Setter
    private String universityID;
    @NotBlank
    private String universityName;
    @NotBlank
    private String rector;
    @NotBlank
    private String webPage;
}
