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
    private String documentID;
    @NotBlank
    private String documentCategoryID;
    @NotBlank
    private String documentName;

    private String documentCategory;
}
