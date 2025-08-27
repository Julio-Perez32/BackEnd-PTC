package ptc2025.backend.Entities.Students;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.StudentDocument.StudentDocumentsEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class  StudentsEntity {

    @Id
    @GenericGenerator(name = "studentID", strategy = "guid")
    @GeneratedValue(generator = "studentID")
    @Column(name = "STUDENTID")
    private String studentID;
    @Column(name = "STUDENTCODE")
    private String studentCode;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    private List<StudentDocumentsEntity> studentDocument = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentCareerEnrollmentsEntity> studentCareerEnrollment = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;
}
