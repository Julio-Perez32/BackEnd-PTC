package ptc2025.backend.Entities.courseEnrollments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;

@Entity
@Table(name = "COURSEENROLLMENTS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CourseEnrollmentEntity {

    @Id
    @GenericGenerator(name = "courseEnrollmentId", strategy = "guid")
    @GeneratedValue(generator = "courseEnrollmentId")
    @Column(name = "ENROLLMENTID")
    private String enrollmentId;

    @Column(name = "STUDENTID", nullable = false)
    private String studentId;

    @Column(name = "OFFERINGID", nullable = false)
    private String offeringId;

    @Column(name = "ENROLLMENTDATE")
    private LocalDate enrollmentDate;

    @Column(name = "GRADE")
    private Double grade;

    @Column(name = "ISACTIVE")
    private Boolean isActive = true;
}
