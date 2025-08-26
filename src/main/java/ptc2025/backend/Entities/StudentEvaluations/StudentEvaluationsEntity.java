package ptc2025.backend.Entities.StudentEvaluations;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTEVALUATIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class StudentEvaluationsEntity {
    @Id
    @GenericGenerator(name = "studentEvaluationID", strategy = "guid")
    @GeneratedValue(generator = "studentEvaluationID")
    @Column(name = "STUDENTEVALUATIONID")
    private String id;
    @Column(name = "COMPONENTID")
    private String componentID;
    @Column(name = "COURSEENROLLMENTID")
    private String courseEnrollmentID;
    @Column(name = "SCORE")
    private int score;
    @Column(name = "FEEDBACK")
    private String feedback;
    @Column(name = "SUBMITTEDAT")
    private LocalDate submittedAt;
}
