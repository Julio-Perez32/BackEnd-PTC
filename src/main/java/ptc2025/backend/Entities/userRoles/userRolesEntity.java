package ptc2025.backend.Entities.userRoles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USERROLES")
@Getter @Setter @ToString @EqualsAndHashCode
public class userRolesEntity {
    @Id
    @GenericGenerator(name = "IDuserRoles", strategy = "guid")
    @GeneratedValue(generator = "IDuserRoles")
    @Column(name = "USERROLEID")
    private String id;
    @Column(name = "USERID")
    private String userID;
    @Column(name = "ROLETYPE")
    private String roleType;
}
