package ptc2025.backend.Entities.CodeGenerators;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CODEGENERATORS")
@Getter
@Setter
@ToString
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

}
/**
 * generatorID
 * entityTypeID
 * correlativeID
 * prefix
 * suffixLength
 * lastAssignedNumber
 * */
