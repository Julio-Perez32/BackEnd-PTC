package ptc2025.backend.Entities.SubjectFamilies;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SUBJECTFAMILIES")
@Getter @Setter @EqualsAndHashCode @ToString
public class SubjectFamiliesEntity {
    @Id
    @GenericGenerator(name = "subjectID", strategy = "guid")
    @GeneratedValue(generator = "subjectID")
    @Column(name = "SUBJECTID")
    private String subjectFamilyID;
    @Column(name = "FACULTYID")
    private String facultyID;
    @Column(name = "SUBJECTPREFIX")
    private String subjectPrefix;
    @Column(name = "RESERVEDSLOTS")
    private Long reservedSlots;
    @Column(name = "STARTINGNUMBER")
    private Long startingNumber;
    @Column(name = "LASTASSIGNEDNUMBER")
    private Long lastAssignedNumber;
}
