package ptc2025.backend.Entities.SubjectDefinitions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBJECTDEFINITIONS")
@Getter @Setter @EqualsAndHashCode @ToString
public class SubjectDefinitionsEntity {

    @Id
    @GenericGenerator(name = "SUBJECTDEFINITIONID", strategy = "guid")
    @GeneratedValue(generator = "SUBJECTDEFINITIONID")
    @Column(name = "SUBJECTID")
    private String SubjectID;
    @Column(name = "SUBJECTFAMILYID")
    private String SubjectFamilyID;
    @Column(name = "SUBJECTNAME")
    private String SubjectName;
    @Column(name = "SUBJECTCODE")
    private String SubjectCode;

    //SubjectDefinitions le da la llave a
    @OneToMany(mappedBy = "SUBJECTDEFINITIONS", cascade = CascadeType.ALL)
    private List<SubjectTeachersEntity> SubjectTeachers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "SUBJECTFAMILYID", referencedColumnName = "SUBJECTFAMILYID")
    private SubjectFamiliesEntity subjectFamilies;
}
