package ptc2025.backend.Respositories.userRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.userRoles.userRolesEntity;

public interface userRoleRepository extends JpaRepository<userRolesEntity, String> {
}
