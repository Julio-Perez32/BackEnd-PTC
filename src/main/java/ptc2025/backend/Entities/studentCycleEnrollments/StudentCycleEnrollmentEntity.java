package ptc2025.backend.Entities.studentCycleEnrollments;

import jakarta.persistence.*;
import lombok.*;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;

import java.time.LocalDate;

@Entity
@Table(name = "studentCycleEnrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCycleEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "studentCycleEnrollmentID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "studentCareerEnrollmentID", referencedColumnName = "studentCareerEnrollmentID")
    private StudentCareerEnrollmentsEntity studentCareerEnrollment;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "registeredAt")
    private LocalDate registeredAt;

    @Column(name = "completedAt")
    private LocalDate completedAt;
}
