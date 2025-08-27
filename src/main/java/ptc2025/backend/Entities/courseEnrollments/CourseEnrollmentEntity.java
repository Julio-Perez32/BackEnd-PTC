package ptc2025.backend.Entities.courseEnrollments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;

import java.time.LocalDate;

@Entity
@Table(name = "COURSEENROLLMENTS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CourseEnrollmentEntity {

    @Id
    @Column(name = "COURSEENROLLMENTID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "COURSEOFFERINGID", referencedColumnName = "COURSEOFFERINGID")
    private CourseOfferingsEntity courseOfferings;

    @ManyToOne
    @JoinColumn(name = "STUDENTCAREERENROLLMENTID", referencedColumnName = "STUDENTCAREERENROLLMENTID")
    private StudentCareerEnrollmentsEntity studentCareerEnrollments;

    @Column(name = "ENROLLMENTSTATUS", nullable = false)
    private String enrollmentStatus;

    @Column(name = "FINALGRADE")
    private Double finalGrade;

    @Column(name = "ENROLLMENTDATE", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "MERITUNIT")
    private Double meritUnit;
}
