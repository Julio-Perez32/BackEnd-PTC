package ptc2025.backend.Entities.Students;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "STUDENTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class StudentsEntity {

    @Id
    @GenericGenerator(name = "studentID", strategy = "guid")
    @GeneratedValue(generator = "studentID")
    @Column(name = "STUDENTID")
    private String studentID;
    @Column(name = "PERSONID")
    private String personID;
    @Column(name = "STUDENTCODE")
    private String studentCode;
}
