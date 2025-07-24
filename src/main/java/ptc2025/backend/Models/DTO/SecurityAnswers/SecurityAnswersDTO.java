package ptc2025.backend.Models.DTO.SecurityAnswers;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityAnswersDTO {
    private String id;
    @NotBlank
    private String questionID;
    @NotBlank
    private String userID;
    @NotBlank
    private String answer;
}
