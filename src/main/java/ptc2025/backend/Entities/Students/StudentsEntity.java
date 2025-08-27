package ptc2025.backend.Entities.Students;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.People.PeopleEntity;
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
    @Column(name = "STUDENTNAME")
    private String studentName;
    @Column(name = "STUDENTCODE")
    private String studentCode;

    @OneToMany(mappedBy = "STUDENTS", cascade = CascadeType.ALL)
    private List<StudentsEntity> Students = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;
}
