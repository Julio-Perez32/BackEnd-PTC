package ptc2025.backend.Entities.systemRoles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import ptc2025.backend.Entities.Users.UsersEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SYSTEMROLES")
@Getter
@Setter
@EqualsAndHashCode
public class SystemRolesEntity {
    @Id
    @GenericGenerator(name = "roleId", strategy = "guid")
    @GeneratedValue(generator = "roleId")
    @Column(name = "ROLEID")
    private String roleId;
    @Column (name = "ROLENAME")
    private String roleName;

    @OneToMany(mappedBy = "systemRoles", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UsersEntity> systemRoles = new ArrayList<>();

    @Override
    public String toString() {
        return "SystemRolesEntity{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName ;
    }
    //se me cerro el push bro
}
