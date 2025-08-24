package ptc2025.backend.Entities.employees;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.People.PeopleEntity;

@Entity
@Table(name = "EMPLOYEES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeeEntity {

    @Id
    @Column(name = "EMPLOYEEID")
    @GenericGenerator(name = "idEmployee", strategy = "guid")
    @GeneratedValue(generator = "idEmployee")
    private String id;

    @Column(name = "PERSONID")
    private String personID;

    @Column(name = "DEPARTMENTID")
    private String deparmentID;

    @Column(name = "EMPLOYEECODE")
    private String employeeCode;

    @Column(name = "EMPLOYEEDETAIL")
    private String EmployeeDetail;

    @OneToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;


}
