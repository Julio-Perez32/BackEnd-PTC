package ptc2025.backend.Entities.People;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Entities.personTypes.personTypesEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PEOPLE")
@Getter @Setter @EqualsAndHashCode @ToString
public class PeopleEntity {

    @Id
    @GenericGenerator(name = "personID", strategy = "guid")
    @GeneratedValue(generator = "personID")
    @Column(name = "PERSONID")
    private String personID;
    @Column(name = "PERSONTYPEID")
    private String persoTypeID;
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

    //personTypes le da su llave a people
    @ManyToOne
    @JoinColumn(name = "PERSONTYPEID", referencedColumnName = "PERSONTYPEID")
    private personTypesEntity personTypes;

    //people le da su llave a
    @OneToMany(mappedBy = "people", cascade = CascadeType.ALL)
    private List<UsersEntity> users = new ArrayList<>();

    @OneToMany(mappedBy = "people", cascade = CascadeType.ALL)
    private List<EmployeeEntity> employee = new ArrayList<>();

    @OneToMany(mappedBy = "people", cascade = CascadeType.ALL)
    private List<StudentsEntity> students = new ArrayList<>();


}
