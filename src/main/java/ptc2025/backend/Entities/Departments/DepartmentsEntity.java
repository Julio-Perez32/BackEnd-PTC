package ptc2025.backend.Entities.Departments;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;

@Entity
@Table(name = "DEPARTMENTS")
@ToString @EqualsAndHashCode @Getter @Setter
public class DepartmentsEntity {

    @Id
    @GenericGenerator(name = "departmentID", strategy = "guid")
    @GeneratedValue(generator = "departmentID")
    @Column(name = "DEPARTMENTID")
    private String departmentID;
    @Column(name = "FACULTYID")
    private String facultyID;
    @Column(name = "DEPARTMENTNAME")
    private String departmentName;
    @Column(name = "DEPARTMENTTYPE")
    private String departmentType;

    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculty;
}
