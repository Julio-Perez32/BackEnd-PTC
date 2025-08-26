package ptc2025.backend.Entities.Students;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.People.PeopleEntity;

@Entity
@Table(name = "STUDENTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class StudentsEntity {

    @Id
    @GenericGenerator(name = "studentID", strategy = "guid")
    @GeneratedValue(generator = "studentID")
    @Column(name = "STUDENTID")
    private String studentID;
    @Column(name = "STUDENTNAME")
    private String studentName;
    @Column(name = "STUDENTCODE")
    private String studentCode;

    @OneToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;
}
