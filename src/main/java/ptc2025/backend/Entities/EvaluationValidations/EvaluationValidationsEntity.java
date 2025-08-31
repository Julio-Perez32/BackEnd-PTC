package ptc2025.backend.Entities.EvaluationValidations;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "EVALUATIONVALIDATION")
@Getter @Setter @EqualsAndHashCode @ToString
public class EvaluationValidationEntity {

    @Id
    @GenericGenerator(name = "VALIDATIONID", strategy = "guid")
    @GeneratedValue(generator = "VALIDATIONID")
    @Column(name = "VALIDATIONID")
    private String validationID;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "REVIEWEDAT")
    private Date reviewedAt;
    @Column(name = "COMMENTS")
    private String comments;
}
