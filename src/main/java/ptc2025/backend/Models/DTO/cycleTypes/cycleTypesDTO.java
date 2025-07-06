package ptc2025.backend.Models.DTO.cycleTypes;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class cycleTypesDTO {
    @NotNull
    private String id;
    @NotNull
    private String universityID;
    @NotBlank
    private String cycleLabel;
}
