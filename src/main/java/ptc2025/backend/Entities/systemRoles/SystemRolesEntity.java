package ptc2025.backend.Entities.systemRoles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYSTEMROLES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SystemRolesEntity {
    @Id
    @GenericGenerator(name = "roleId", strategy = "guid")
    @GeneratedValue(generator = "roleId")
    @Column(name = "ROLEID")
    private String roleId;
    @Column (name = "ROLENAME")
    private String roleName;
}
