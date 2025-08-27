package ptc2025.backend.Entities.studentCycleEnrollments;

import jakarta.persistence.*;
import lombok.*;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTCYCLEENROLLMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCycleEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "STUDENTCYCLEENROLLMENTID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "STUDENTCAREERENROLLMENTID", referencedColumnName = "STUDENTCAREERENROLLMENTID")
    private StudentCareerEnrollmentsEntity studentCareerEnrollment;

    @ManyToOne
    @JoinColumn(name = "YEARCYCLEID", referencedColumnName = "YEARCYCLEID")
    private YearCyclesEntity yearCycles;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "REGISTEREDAT")
    private LocalDate registeredAt;

    @Column(name = "COMPLETEDAT")
    private LocalDate completedAt;
}
