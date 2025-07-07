package ptc2025.backend.Entities.EventTypes;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "EVENTTYPES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class eventTypesEntity {
    @Id
    @GenericGenerator(name = "idEvent", strategy = "guid")
    @GeneratedValue(generator = "idEvent")
    @Column(name = "EVENTTYPEID")
    private String eventTypeID;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "TYPENAME")
    private String typeName;
}
