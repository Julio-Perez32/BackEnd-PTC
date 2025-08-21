package ptc2025.backend.Entities.studentCycleEnrollments;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "STUDENTCYCLEENROLLMENTS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StudentCycleEnrollmentEntity {

    @Id
    @Column(name = "ENROLLMENTID")
    private String id;

    @Column(name = "STUDENTID", nullable = false)
    private String studentId;

    @Column(name = "CYCLEID", nullable = false)
    private String cycleId;

    @Column(name = "ENROLLMENTDATE")
    private LocalDate enrollmentDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
