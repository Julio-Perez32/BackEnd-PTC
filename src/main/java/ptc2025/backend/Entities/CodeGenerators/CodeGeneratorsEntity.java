package ptc2025.backend.Entities.CodeGenerators;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.EntityType.EntityTypesEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;

@Entity
@Table(name = "CODEGENERATORS")
@Getter
@Setter
@EqualsAndHashCode
public class CodeGeneratorsEntity {
    @Id
    @GenericGenerator(name = "generatorID", strategy = "guid")
    @GeneratedValue(generator = "generatorID")
    private String generatorID;

    @Column(name = "PREFIX")
    private  String prefix;

    @Column(name = "SUFFIXLENGTH")
    private Integer suffixLength;

    @Column(name = "LASTASSIGNEDNUMBER")
    private Integer lastAssignedNumber;

    @ManyToOne
    @JoinColumn(name = "ENTITYTYPEID", referencedColumnName = "ENTITYTYPEID")
    private EntityTypesEntity entityType;

    @ManyToOne
    @JoinColumn(name = "CORRELATIVEID", referencedColumnName = "CORRELATIVEID")
    private facultyCorrelativesEntity facultyCorrelative;

    @Override
    public String toString() {
        return "CodeGeneratorsEntity{" +
                "generatorID='" + generatorID + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffixLength=" + suffixLength +
                ", lastAssignedNumber=" + lastAssignedNumber +
                ", entityType=" + entityType +
                ", facultyCorrelative=" + facultyCorrelative +
                '}';
    }
}
/**
 * generatorID
 * entityTypeID
 * correlativeID
 * prefix
 * suffixLength
 * lastAssignedNumber
 * */
