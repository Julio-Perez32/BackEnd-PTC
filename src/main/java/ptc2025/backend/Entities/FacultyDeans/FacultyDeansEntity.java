package ptc2025.backend.Entities.FacultyDeans;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;

import java.time.LocalDate;

@Entity
@Table(name = "FACULTYDEANS")
@Getter @Setter @ToString @EqualsAndHashCode
public class FacultyDeansEntity {
    @Id
    @GenericGenerator(name = "idfacultydeans", strategy = "guid")
    @GeneratedValue(generator = "idfacultydeans")
    @Column(name = "FACULTYDEANID")
    private String id;
    @Column(name = "STARTDATE")
    private LocalDate startDate;
    @Column(name = "ENDDATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculty;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEEID", referencedColumnName = "EMPLOYEEID")
    private EmployeeEntity employee;
}
