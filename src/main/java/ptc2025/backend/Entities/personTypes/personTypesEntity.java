package ptc2025.backend.Entities.personTypes;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PERSONTYPES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class personTypesEntity {
    @Id
    @GenericGenerator(name = "personTypeID", strategy = "guid")
    @GeneratedValue(generator = "personTypeID")
    @Column(name = "PERSONTYPEID")
    private String personTypeID;

    @Column(name = "PERSONTYPE")
    private String personType;
}
