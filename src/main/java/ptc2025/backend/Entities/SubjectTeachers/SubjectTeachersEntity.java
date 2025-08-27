package ptc2025.backend.Entities.SubjectTeachers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBJECTTEACHERS")
@Getter @Setter @EqualsAndHashCode @ToString
public class SubjectTeachersEntity {

    @Id
    @GenericGenerator(name = "SUBJECTTEACHERID", strategy = "guid")
    @GeneratedValue(generator = "SUBJECTTEACHERID")
    @Column(name = "SUBJECTTEACHERID")
    private String SubjectTeacherID;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEEID", referencedColumnName = "EMPLOYEEID")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "SUBJECTID")
    private SubjectDefinitionsEntity subjectDefinitions;
}
