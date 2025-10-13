package ptc2025.backend.Models.DTO.EvaluationPlans;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString @EqualsAndHashCode
public class EvaluationPlansDTO {
    private String evaluationPlanID;

    @NotBlank(message = "courseOfferingID es obligatorio")
    private String courseOfferingID;

    @NotBlank(message = "planName es obligatorio")
    @Size(min = 4, max = 80, message = "planName debe tener entre 4 y 80 caracteres")
    private String planName;

    @Size(max = 200, message = "description no debe exceder 200 caracteres")
    private String description;


    private LocalDate createdAt;

    private String courseoffering;
}

