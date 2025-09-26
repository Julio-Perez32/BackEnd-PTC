package ptc2025.backend.Entities.Users;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.Notification.NotificationEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter @Setter @ToString @EqualsAndHashCode
public class UsersEntity {

    @Id
    @GenericGenerator(name = "IDusers", strategy = "guid")
    @GeneratedValue(generator = "IDusers")
    @Column(name = "USERID")
    private String id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String contrasena;
    @Column(name = "IMAGENURLUSERS")
    private String imageUrlUser;


    @ManyToOne
    @JoinColumn(name = "UNIVERSITYID", referencedColumnName = "UNIVERSITYID")
    private UniversityEntity university;

    @ManyToOne
    @JoinColumn(name = "PERSONID", referencedColumnName = "PERSONID")
    private PeopleEntity people;

    @ManyToOne
    @JoinColumn(name = "ROLEID", referencedColumnName = "ROLEID")
    private SystemRolesEntity systemRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NotificationEntity> notification = new ArrayList<>();

    @OneToMany(mappedBy = "userID", cascade = CascadeType.ALL)
    private List<StudentEvaluationsEntity> studentEvaluations = new ArrayList<>();
}
