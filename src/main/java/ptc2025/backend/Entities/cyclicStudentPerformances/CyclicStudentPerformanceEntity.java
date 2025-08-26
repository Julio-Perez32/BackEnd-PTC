package ptc2025.backend.Entities.cyclicStudentPerformances;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;

import java.time.LocalDate;

@Entity
@Table(name = "CYCLICSTUDENTPERFORMANCES")
@Getter
@Setter
public class CyclicStudentPerformanceEntity {

    @Id
    @Column(name = "PERFORMANCEID")
    private String performanceID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENTCYCLEENROLLMENTID", nullable = false)
    private StudentCycleEnrollmentEntity studentCycleEnrollment;

    @Column(name = "TOTALVALUEUNITS")
    private Integer totalValueUnits;

    @Column(name = "TOTALMERITUNIT")
    private Integer totalMeritUnit;

    @Column(name = "MERITUNITCOEFFICIENT")
    private Double meritUnitCoefficient;

    @Column(name = "COMPUTEDAT")
    private LocalDate computedAt;
}
