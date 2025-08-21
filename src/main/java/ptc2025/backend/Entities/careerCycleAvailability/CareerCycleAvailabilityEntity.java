package ptc2025.backend.Entities.careerCycleAvailability;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CAREERCYCLEAVAILABILITY")
@Getter
@Setter
public class CareerCycleAvailabilityEntity {

    @Id
    @Column(name = "AVAILABILITYID")
    private String id;

    @Column(name = "CAREERID", nullable = false)
    private String careerId;

    @Column(name = "ACADEMICYEARID", nullable = false)
    private String academicYearId;

    @Column(name = "CYCLECODE", nullable = false)
    private String cycleCode;

    @Column(name = "MAXCAPACITY")
    private Integer maxCapacity;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
