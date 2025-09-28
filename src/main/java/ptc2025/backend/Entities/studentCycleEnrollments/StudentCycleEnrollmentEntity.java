package ptc2025.backend.Entities.studentCycleEnrollments;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTCYCLEENROLLMENTS")
@Getter
@Setter
@EqualsAndHashCode
public class StudentCycleEnrollmentEntity {

    @Id
    @GenericGenerator(name = "StudentCycleEnrollmentID", strategy = "guid")
    @GeneratedValue(generator = "StudentCycleEnrollmentID")
    @Column(name = "STUDENTCYCLEENROLLMENTID")
    private String id;


    @ManyToOne
    @JoinColumn(name = "YEARCYCLEID", referencedColumnName = "YEARCYCLEID")
    private YearCyclesEntity yearCycles;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "REGISTEREDAT")
    private LocalDate registeredAt;

    @Column(name = "COMPLETEDAT")
    private LocalDate completedAt;

    @ManyToOne
    @JoinColumn(name = "STUDENTCAREERENROLLMENTID", referencedColumnName = "STUDENTCAREERENROLLMENTID")
    private StudentCareerEnrollmentsEntity studentCareerEnrollment;

    @Override
    public String toString() {
        return "StudentCycleEnrollmentEntity{" +
                "id='" + id + '\'' +
                ", yearCycles=" + yearCycles +
                ", status='" + status + '\'' +
                ", registeredAt=" + registeredAt +
                ", completedAt=" + completedAt;
    }
}
