package ptc2025.backend.Models.DTO.EvaluationValidations;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @EqualsAndHashCode @ToString
public class EvaluationValidationsDTO {

    private String validationID;
    @NotBlank
    private String studentEvaluationID;
    private String createdBy;
    private String reviewedBy;
    @NotBlank @Size(max = 20)
    private String status;
    @PastOrPresent
    private Date reviewedAt;
    @Size(max = 500)
    private String comments;

    private String userFirstName;
    private String userLastName;
}
