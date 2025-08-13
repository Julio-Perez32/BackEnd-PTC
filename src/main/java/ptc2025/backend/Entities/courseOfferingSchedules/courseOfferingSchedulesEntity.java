package ptc2025.backend.Entities.courseOfferingSchedules;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "COURSEOFFERINGSCHEDULES")
@Getter @Setter @ToString @EqualsAndHashCode
public class courseOfferingSchedulesEntity {
    @Id
    @GenericGenerator(name = "IDcourseOfferingSchedules", strategy = "guid")
    @GeneratedValue(generator = "IDcourseOfferingSchedules")
    @Column(name = "COURSEOFFERINGSCHEDULEID")
    private String id;
    @Column(name = "COURSEOFFERINGID")
    private String courseOfferingID;
    @Column(name = "WEEKDAY")
    private String weekday;
    @Column(name = "STARTTIME")
    private String startTime;
    @Column(name = "ENDTIME")
    private String endTime;
    @Column(name = "CLASSROOM")
    private String classroom;

}
