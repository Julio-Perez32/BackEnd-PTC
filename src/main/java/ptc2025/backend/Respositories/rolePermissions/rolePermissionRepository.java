package ptc2025.backend.Respositories.rolePermissions;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.rolePermissions.rolePermissionsEntity;

public interface rolePermissionRepository extends JpaRepository<rolePermissionsEntity, String > {
}
