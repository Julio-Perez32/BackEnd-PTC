package ptc2025.backend.Entities.SubjectFamilies;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SUBJECTFAMILIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class SubjectFamiliesEntity {
    @Id
    @GenericGenerator(name = "subjectID", strategy = "guid")
    @GeneratedValue(generator = "subjectID")
    @Column(name = "SUBJECTFAMILYID")
    private String subjectFamilyID;
    @Column(name = "SUBJECTPREFIX")
    private String subjectPrefix;
    @Column(name = "RESERVEDSLOTS")
    private Long reservedSlots;
    @Column(name = "STARTINGNUMBER")
    private Long startingNumber;
    @Column(name = "LASTASSIGNEDNUMBER")
    private Long lastAssignedNumber;

    //SubjectFamilies le da la llave a
    @OneToMany(mappedBy = "SUBJECTFAMILIES", cascade = CascadeType.ALL)
    private List<SubjectDefinitionsEntity> SubjectDefinitions = new ArrayList<>();

    //Faculties le da la llave a SubjectTeachers
    @ManyToOne
    @JoinColumn(name = "FACULTYID", referencedColumnName = "FACULTYID")
    private FacultiesEntity faculty;
}
