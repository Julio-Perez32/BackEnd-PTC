package ptc2025.backend.Entities.CourseOfferingsTeachers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;

@Entity
@Table(name = "COURSEOFFERINGTEACHERS")
@Getter @Setter @EqualsAndHashCode
public class CourseOfferingsTeachersEntity {

    @Id
    @GenericGenerator(name = "CourseOfferingTeacherID", strategy = "guid")
    @GeneratedValue(generator = "CourseOfferingTeacherID")
    @Column(name = "COURSEOFFERINGTEACHERID")
    private String CourseOfferingTeacherID;

    @ManyToOne
    @JoinColumn(name = "COURSEOFFERINGID", referencedColumnName = "COURSEOFFERINGID")
    private CourseOfferingsEntity courseOfferings;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEEID", referencedColumnName = "EMPLOYEEID")
    private EmployeeEntity employee;

    @Override
    public String toString() {
        return "CourseOfferingsTeachersEntity{" +
                "CourseOfferingTeacherID='" + CourseOfferingTeacherID + '\'' +
                ", courseOfferings=" + courseOfferings +
                ", employee=" + employee +
                '}';
    }
}
