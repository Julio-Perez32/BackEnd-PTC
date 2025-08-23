package ptc2025.backend.Respositories.EmployeeRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.EmployeeRoles.EmployeeRolesEntity;

public interface EmployeeRolesRepository extends JpaRepository<EmployeeRolesEntity, String> {
}
