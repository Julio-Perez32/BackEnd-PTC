package ptc2025.backend.Entities.careerCycleAvailability;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.careers.CareerEntity;

@Entity
@Table(name = "CAREERCYCLEAVAILABILITY")
@Getter
@Setter
public class CareerCycleAvailabilityEntity {

    @Id
    @Column(name = "CAREERCYCLEAVAILABILITYID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "CAREERID", referencedColumnName = "CAREERID")
    private CareerEntity career;

    @ManyToOne
    @JoinColumn(name = "YEARCYCLEID", referencedColumnName = "YEARCYCLEID")
    private YearCyclesEntity yearCycles;
}
