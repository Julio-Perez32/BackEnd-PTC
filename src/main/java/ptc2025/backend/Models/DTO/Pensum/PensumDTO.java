package ptc2025.backend.Models.DTO.Pensum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @EqualsAndHashCode @ToString
public class PensumDTO {

    private String PensumID;
    @NotBlank
    private String CareerID;
    @NotBlank @Size(max = 20)
    private String Version;
    @NotBlank @Size(max = 4)
    private Long EffectiveYear;
}
