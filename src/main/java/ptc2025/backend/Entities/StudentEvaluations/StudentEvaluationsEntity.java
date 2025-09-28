package ptc2025.backend.Entities.StudentEvaluations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.EvaluationValidations.EvaluationValidationsEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STUDENTEVALUATIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class StudentEvaluationsEntity {
    @Id
    @GenericGenerator(name = "studentEvaluationID", strategy = "guid")
    @GeneratedValue(generator = "studentEvaluationID")
    @Column(name = "STUDENTEVALUATIONID")
    private String id;
    @Column(name = "SCORE")
    private int score;
    @Column(name = "FEEDBACK")
    private String feedback;
    @Column(name = "SUBMITTEDAT")
    private LocalDate submittedAt;

    @OneToMany(mappedBy = "studentEvaluationID", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EvaluationValidationsEntity> evaluationValidations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "COMPONENTID", referencedColumnName = "COMPONENTID")
    private PlanComponentsEntity planComponents;

    @ManyToOne
    @JoinColumn(name = "COURSEENROLLMENTID", referencedColumnName = "COURSEENROLLMENTID")
    private CourseEnrollmentEntity courseEnrollment;

    @ManyToOne
    @JoinColumn(name = "CREATEDBY", referencedColumnName = "USERID")
    private UsersEntity userID;


    @Override
    public String toString() {
        return "StudentEvaluationsEntity{" +
                "id='" + id + '\'' +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                ", submittedAt=" + submittedAt ;
    }
    //se me cerro el push bro
}
