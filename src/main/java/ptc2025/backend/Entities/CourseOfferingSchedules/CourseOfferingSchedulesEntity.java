package ptc2025.backend.Entities.CourseOfferingSchedules;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;

@Entity
@Table(name = "COURSEOFFERINGSCHEDULES")
@Getter @Setter @ToString @EqualsAndHashCode
public class CourseOfferingSchedulesEntity {
    @Id
    @GenericGenerator(name = "IDcourseOfferingSchedules", strategy = "guid")
    @GeneratedValue(generator = "IDcourseOfferingSchedules")
    @Column(name = "COURSEOFFERINGSCHEDULEID")
    private String id;
    @Column(name = "WEEKDAY")
    private String weekday;
    @Column(name = "STARTTIME")
    private String startTime;
    @Column(name = "ENDTIME")
    private String endTime;
    @Column(name = "CLASSROOM")
    private String classroom;

    @ManyToOne
    @JoinColumn(name = "COURSEOFFERINGID", referencedColumnName = "COURSEOFFERINGID")
    private CourseOfferingsEntity courseOfferings;
}
