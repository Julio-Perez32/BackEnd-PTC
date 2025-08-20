package ptc2025.backend.Entities.SubjectDefinitions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

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
}
