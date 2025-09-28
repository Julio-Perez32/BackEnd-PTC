package ptc2025.backend.Entities.EmployeeRoles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;


//se borro lo mas probable
@Entity
@Table(name = "EMPLOYEEROLES")
@Getter @Setter @EqualsAndHashCode
public class EmployeeRolesEntity {
    @Id
    @GenericGenerator(name = "IDemployeeRoles", strategy = "guid")
    @GeneratedValue(generator = "IDemployeeRoles")
    @Column(name = "ROLEID")
    private String id;
    @Column(name = "UNIVERSITYID")
    private String universityID;
    @Column(name = "ROLENAME")
    private String roleName;
    @Column(name = "ROLETYPE")
    private String roleType;

    @Override
    public String toString() {
        return "EmployeeRolesEntity{" +
                "id='" + id + '\'' +
                ", universityID='" + universityID + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleType='" + roleType + '\'' +
                '}';
    }
}
