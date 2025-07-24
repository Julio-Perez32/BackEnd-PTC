package ptc2025.backend.Entities.userRoles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEntity {

    @Id
    @Column(name = "ROLEID")
    private String id;

    @Column(name = "ROLENAME", nullable = false)
    private String name;

    @Column(name = "ROLEDESCRIPTION")
    private String description;

    @Column(name = "ACCESSLEVEL")
    private Integer accessLevel;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
