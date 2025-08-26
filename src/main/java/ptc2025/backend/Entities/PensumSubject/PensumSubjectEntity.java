package ptc2025.backend.Entities.PensumSubject;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.careers.CareerEntity;

@Entity
@Table(name = "PENSUMSUBJECTS")
@Getter @Setter @EqualsAndHashCode @ToString
public class PensumSubjectEntity {

    @Id
    @GenericGenerator(name = "PENSUMSUBJECTID", strategy = "guid")
    @GeneratedValue(generator = "PENSUMSUBJECTID")
    @Column(name = "PENSUMSUBJECTID")
    private String PensumSubjectID;
    @Column(name = "VALUEUNITS")
    private Long ValueUnits;
    @Column(name = "ISREQUIRED")
    private Character IsRequired;

    @ManyToOne
    @JoinColumn(name = "PENSUMID", referencedColumnName = "PENSUMID")
    private PensumEntity pensum;

    @ManyToOne
    @JoinColumn(name = "SUBJECTID", referencedColumnName = "SUBJECTID")
    private SubjectDefinitionsEntity subjectDefinitions;
}
