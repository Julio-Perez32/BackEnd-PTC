package ptc2025.backend.Entities.StudentEvaluations;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;

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
    @Column(name = "SCORE")
    private int score;
    @Column(name = "FEEDBACK")
    private String feedback;
    @Column(name = "SUBMITTEDAT")
    private LocalDate submittedAt;

    @ManyToOne
    @JoinColumn(name = "COMPONENTID", referencedColumnName = "COMPONENTID")
    private PlanComponentsEntity planComponents;

    @ManyToOne
    @JoinColumn(name = "COURSEENROLLMENTID", referencedColumnName = "COURSEENROLLMENTID")
    private CourseEnrollmentEntity courseEnrollment;
}
