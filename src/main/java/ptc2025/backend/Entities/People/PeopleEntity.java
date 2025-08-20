package ptc2025.backend.Entities.People;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "PEOPLE")
@Getter @Setter @EqualsAndHashCode @ToString
public class PeopleEntity {

    @Id
    @GenericGenerator(name = "personID", strategy = "guid")
    @GeneratedValue(generator = "personID")
    @Column(name = "PERSONID")
    private String personID;
    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "BIRTHDATE")
    private Date birthDate;
    @Column(name = "CONTACTEMAIL")
    private String contactEmail;
    @Column(name = "PHONE")
    private String phone;
}
