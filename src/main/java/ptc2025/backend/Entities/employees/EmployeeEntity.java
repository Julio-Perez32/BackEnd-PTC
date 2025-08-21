package ptc2025.backend.Entities.employees;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "EMPLOYEES")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeeEntity {

    @Id
    @Column(name = "EMPLOYEEID")
    private String id;

    @Column(name = "FULLNAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ISACTIVE")
    private Boolean isActive;
}
