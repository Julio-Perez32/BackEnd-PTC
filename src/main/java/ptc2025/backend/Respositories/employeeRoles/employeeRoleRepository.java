package ptc2025.backend.Respositories.employeeRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.employeeRoles.employeeRolesEntity;

public interface employeeRoleRepository extends JpaRepository<employeeRolesEntity, String> {
}
