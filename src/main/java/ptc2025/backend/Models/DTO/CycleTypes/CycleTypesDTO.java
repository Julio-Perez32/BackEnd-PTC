package ptc2025.backend.Models.DTO.CycleTypes;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CycleTypesDTO {
    private String id;
    @NotNull
    private String universityID;
    @NotBlank
    private String cycleLabel;

    //campo para el nombre
    private String universityName;
}