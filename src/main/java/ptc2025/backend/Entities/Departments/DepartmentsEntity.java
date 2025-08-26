package ptc2025.backend.Entities.Departments;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEPARTMENTS")
@ToString @EqualsAndHashCode @Getter @Setter
public class DepartmentsEntity {

    @Id
    @GenericGenerator(name = "departmentID", strategy = "guid")
    @GeneratedValue(generator = "departmentID")
    @Column(name = "DEPARTMENTID")
    private String departmentID;
    @Column(name = "DEPARTMENTNAME")
    private String departmentName;
    @Column(name = "DEPARTMENTTYPE")
    private String departmentType;

    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculty;

    @OneToMany(mappedBy = "departments", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employee = new ArrayList<>();
}
