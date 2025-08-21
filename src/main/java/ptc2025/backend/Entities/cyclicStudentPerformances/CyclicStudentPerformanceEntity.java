package ptc2025.backend.Entities.cyclicStudentPerformances;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CYCLICSTUDENTPERFORMANCES")
@Getter
@Setter
public class CyclicStudentPerformanceEntity {

    @Id
    @Column(name = "PERFORMANCEID")
    private String id;

    @Column(name = "STUDENTID", nullable = false)
    private String studentId;

    @Column(name = "CYCLECODE", nullable = false)
    private String cycleCode;

    @Column(name = "ACADEMICYEARID")
    private String academicYearId;

    @Column(name = "AVERAGEGRADE")
    private Double averageGrade;

    @Column(name = "PASSED")
    private Boolean passed;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
