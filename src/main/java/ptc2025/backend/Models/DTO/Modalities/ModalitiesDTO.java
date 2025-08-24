package ptc2025.backend.Models.DTO.Modalities;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModalitiesDTO {
    private String id;
    @NotBlank
    private String universityID;
    @NotBlank
    private String modalityName;

    private String universityName;
}
