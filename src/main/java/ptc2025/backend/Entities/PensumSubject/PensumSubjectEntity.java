package ptc2025.backend.Entities.PensumSubject;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PENSUMSUBJECTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class PensumSubjectEntity {

    @Id
    @GenericGenerator(name = "PENSUMSUBJECTID", strategy = "guid")
    @GeneratedValue(generator = "PENSUMSUBJECTID")
    @Column(name = "PENSUMSUBJECTID")
    private String PensumSubjectID;
    @Column(name = "PENSUMID")
    private String PensumID;
    @Column(name = "SUBJECTID")
    private String SubjectID;
    @Column(name = "VALUEUNITS")
    private Long ValueUnits;
    @Column(name = "ISREQUIRED")
    private Character IsRequired;
}
