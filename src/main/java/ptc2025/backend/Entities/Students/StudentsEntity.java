package ptc2025.backend.Entities.Students;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Getter @Setter @EqualsAndHashCode
public class  StudentsEntity {

    @Id
    @GenericGenerator(name = "studentID", strategy = "guid")
    @GeneratedValue(generator = "studentID")
    @Column(name = "STUDENTID")
    private String studentID;
    @Column(name = "STUDENTCODE")
    private String studentCode;

    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentDocumentsEntity> studentDocument = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<StudentCareerEnrollmentsEntity> studentCareerEnrollment = new ArrayList<>();

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;

    @Override
    public String toString() {
        return "StudentsEntity{" +
                "studentID='" + studentID + '\'' +
                ", studentCode='" + studentCode;
    }
    //se me cerro el push bro
}
