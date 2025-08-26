package ptc2025.backend.Entities.employees;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "EMPLOYEECODE")
    private String employeeCode;

    @Column(name = "EMPLOYEEDETAIL")
    private String EmployeeDetail;

    @OneToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;

    @OneToMany(mappedBy = "employees", cascade = CascadeType.ALL)
    private List<FacultyDeansEntity> facultyDeans = new ArrayList<>();

    @OneToMany(mappedBy = "employees", cascade = CascadeType.ALL)
    private List<CourseOfferingsTeachersEntity> courseOfferingsTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "employees", cascade = CascadeType.ALL)
    private List<SubjectTeachersEntity> subjectTeachers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "DEPARTMENTID", referencedColumnName = "DEPARTMENTID")
    private DepartmentsEntity departments;


}
