package ptc2025.backend.Entities.CourseOfferingsTeachers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "COURSEOFFERINGTEACHERS")
@Getter @Setter @EqualsAndHashCode @ToString
public class CourseOfferingsTeachersEntity {

    @Id
    @GenericGenerator(name = "CourseOfferingTeacherID", strategy = "guid")
    @GeneratedValue(generator = "CourseOfferingTeacherID")
    @Column(name = "COURSEOFFERINGTEACHERID")
    private String CourseOfferingTeacherID;
    @Column(name = "COURSEOFFERINGID")
    private String CourseOfferingID;
    @Column(name = "EMPLOYEEID")
    private String EmployeeID;
}
