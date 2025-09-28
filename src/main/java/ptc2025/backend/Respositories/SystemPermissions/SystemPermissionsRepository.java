package ptc2025.backend.Respositories.SystemPermissions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SystemPermissions.SystemPermissionsEntity;

public interface SystemPermissionsRepository extends JpaRepository<SystemPermissionsEntity, String> {
    Page<SystemPermissionsEntity> findAll(Pageable pageable);
}
