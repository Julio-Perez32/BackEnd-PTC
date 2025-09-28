package ptc2025.backend.Entities.EvaluationValidations;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;

import java.util.Date;

@Entity
@Table(name = "EVALUATIONVALIDATIONS")
@Getter @Setter @EqualsAndHashCode
public class EvaluationValidationsEntity {

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

    //Faculties le da la llave a SubjectTeachers
    @ManyToOne
    @JoinColumn(name = "STUDENTEVALUATIONID", referencedColumnName = "STUDENTEVALUATIONID")
    private StudentEvaluationsEntity studentEvaluationID;

    @ManyToOne
    @JoinColumn(name = "CREATEDBY", referencedColumnName = "STUDENTEVALUATIONID")
    private StudentEvaluationsEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "REVIEWEDBY", referencedColumnName = "STUDENTEVALUATIONID")
    private StudentEvaluationsEntity reviewedBy;

    @Override
    public String toString() {
        return "EvaluationValidationsEntity{" +
                "validationID='" + validationID + '\'' +
                ", status='" + status + '\'' +
                ", reviewedAt=" + reviewedAt +
                ", comments='" + comments + '\'' +
                ", studentEvaluationID=" + studentEvaluationID +
                ", createdBy=" + createdBy +
                ", reviewedBy=" + reviewedBy +
                '}';
    }
}
