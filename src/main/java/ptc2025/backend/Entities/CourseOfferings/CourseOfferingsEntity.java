package ptc2025.backend.Entities.CourseOfferings;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "COURSEOFFERINGS")
@Getter @Setter @EqualsAndHashCode @ToString
public class CourseOfferingsEntity {

    @Id
    @GenericGenerator(name = "CourseOfferingID", strategy = "guid")
    @GeneratedValue(generator = "CourseOfferingID")
    @Column(name = "COURSEOFFERINGID")
    private String CourseOfferingID;
    @Column(name = "SUBJECTID")
    private String SubjectID;
    @Column(name = "YEARCYCLEID")
    private String YearCycleID;
}
