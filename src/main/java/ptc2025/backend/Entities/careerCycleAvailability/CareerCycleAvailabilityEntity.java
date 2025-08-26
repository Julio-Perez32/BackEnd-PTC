package ptc2025.backend.Entities.careerCycleAvailability;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ptc2025.backend.Entities.careers.CareerEntity;

@Entity
@Table(name = "CAREERCYCLEAVAILABILITY")
@Getter
@Setter
public class CareerCycleAvailabilityEntity {

    @Id
    @Column(name = "CAREERCYCLEAVAILABILITYID")
    private String id;

    @Column(name = "YEARCYCLEID", nullable = false)
    private String yearCycleId;

    @ManyToOne
    @JoinColumn(name = "CAREERID", nullable = false)
    private CareerEntity career;
}
