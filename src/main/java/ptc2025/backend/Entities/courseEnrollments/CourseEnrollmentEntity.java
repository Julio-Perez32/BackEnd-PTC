package ptc2025.backend.Entities.courseEnrollments;

import jakarta.persistence.*;
import lombok.*;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;

import java.time.LocalDate;

@Entity
@Table(name = "courseEnrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "courseEnrollmentID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "courseOfferingID", referencedColumnName = "courseOfferingID")
    private CourseOfferingsEntity courseOfferings;

    @ManyToOne
    @JoinColumn(name = "studentCareerEnrollmentID", referencedColumnName = "studentCareerEnrollmentID")
    private StudentCareerEnrollmentsEntity studentCareerEnrollments;

    @Column(name = "enrollmentStatus")
    private String enrollmentStatus;

    @Column(name = "finalGrade")
    private Double finalGrade;

    @Column(name = "enrollmentDate")
    private LocalDate enrollmentDate;

    @Column(name = "meritUnit")
    private Double meritUnit;
}
