package ptc2025.backend.Models.DTO.Documents;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter
@ToString @EqualsAndHashCode
public class DocumentDTO {

    @NotBlank(message = "El ID del documento es obligatorio")
    private String id;

    @NotBlank(message = "El nombre del documento es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String type;

    private LocalDate uploadDate;

    @NotBlank(message = "El propietario es obligatorio")
    private String ownerId;

    private Boolean isActive = true;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoriesId;
}
