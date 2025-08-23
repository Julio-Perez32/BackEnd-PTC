package ptc2025.backend.Entities.SystemPermissions;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYSTEMPERMISSIONS")
@Getter @Setter @ToString @EqualsAndHashCode
public class SystemPermissionsEntity {
    @Id
    @GenericGenerator(name = "IDsystemPermissions", strategy = "guid")
    @GeneratedValue(generator = "IDsystemPermissions")
    @Column(name = "PERMISSIONID")
    private String id;
    @Column(name = "CATEGORYID")
    private String categoryID;
    @Column(name = "PERMISSIONNAME")
    private String permissionName;
    @Column(name = "MANAGEPERMISSIONS")
    private boolean managePermissions;
}
