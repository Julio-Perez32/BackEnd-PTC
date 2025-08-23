package ptc2025.backend.Entities.RolePermissions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ROLEPERMISSIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class RolePermissionsEntity {
    @Id
    @GenericGenerator(name = "idRolePermissions", strategy = "guid")
    @GeneratedValue(generator = "idRolePermissions")
    @Column(name = "ROLEPERMISSIONID")
    private String id;
    @Column(name = "PERMISSIONID")
    private String permissionID;
    @Column(name = "ROLEID")
    private String roleID;
}
