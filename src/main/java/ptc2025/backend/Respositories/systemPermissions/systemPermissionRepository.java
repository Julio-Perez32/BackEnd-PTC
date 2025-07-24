package ptc2025.backend.Respositories.systemPermissions;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.systemPermissions.systemPermissionsEntity;

public interface systemPermissionRepository extends JpaRepository<systemPermissionsEntity, String> {
}
