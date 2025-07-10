package ptc2025.backend.Entities.Modalities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "MODALITIES")
@Getter @Setter @ToString @EqualsAndHashCode
public class ModalitiesEntity {

    @Id
    @GenericGenerator(name = "idModalities", strategy = "guid")
    @GeneratedValue(generator = "idModalities")
    @Column(name = "MODALITYID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "MODALITYNAME")
    private String modalityName;
}
