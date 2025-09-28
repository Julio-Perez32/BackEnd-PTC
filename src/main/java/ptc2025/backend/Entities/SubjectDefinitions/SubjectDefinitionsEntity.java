package ptc2025.backend.Entities.SubjectDefinitions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.PensumSubject.PensumSubjectEntity;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBJECTDEFINITIONS")
@Getter @Setter @EqualsAndHashCode
public class SubjectDefinitionsEntity {

    @Id
    @GenericGenerator(name = "SUBJECTDEFINITIONID", strategy = "guid")
    @GeneratedValue(generator = "SUBJECTDEFINITIONID")
    @Column(name = "SUBJECTID")
    private String SubjectID;
    @Column(name = "SUBJECTNAME")
    private String SubjectName;
    @Column(name = "SUBJECTCODE")
    private String SubjectCode;

    //SubjectDefinitions le da la llave a
    @OneToMany(mappedBy = "subjectDefinitions", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SubjectTeachersEntity> SubjectTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "subjectDefinitions", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PensumSubjectEntity> pensumSubject = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "SUBJECTFAMILYID", referencedColumnName = "SUBJECTFAMILYID")
    private SubjectFamiliesEntity subjectFamilies;

    @Override
    public String toString() {
        return "SubjectDefinitionsEntity{" +
                "SubjectID='" + SubjectID + '\'' +
                ", SubjectName='" + SubjectName + '\'' +
                ", SubjectCode='" + SubjectCode;
    }
}
