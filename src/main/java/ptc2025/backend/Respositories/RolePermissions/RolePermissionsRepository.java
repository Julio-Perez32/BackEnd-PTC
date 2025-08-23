package ptc2025.backend.Respositories.RolePermissions;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.RolePermissions.RolePermissionsEntity;

public interface RolePermissionsRepository extends JpaRepository<RolePermissionsEntity, String > {
}
