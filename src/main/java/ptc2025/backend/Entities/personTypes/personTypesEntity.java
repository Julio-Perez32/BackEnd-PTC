package ptc2025.backend.Entities.personTypes;

import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.People.PeopleEntity;

import java.util.ArrayList;
import java.util.List;

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

    //La llave de personType para people
    @OneToMany(mappedBy = "personTypes", cascade = CascadeType.ALL)
    private List<PeopleEntity> people = new ArrayList<>();

}
