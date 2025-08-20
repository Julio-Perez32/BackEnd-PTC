package ptc2025.backend.Entities.SubjectTeachers;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SUBJECTTEACHERS")
@Getter @Setter @EqualsAndHashCode @ToString
public class SubjectTeachersEntity {

    @Id
    @GenericGenerator(name = "SUBJECTTEACHERID", strategy = "guid")
    @GeneratedValue(generator = "SUBJECTTEACHERID")
    @Column(name = "SUBJECTTEACHERID")
    private String SubjectTeacherID;
    @Column(name = "SUBJECTID")
    private String SubjectID;
    @Column(name = "EMPLOYEEID")
    private String EmployeeID;
}
