package ptc2025.backend.Respositories.systemRoles;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;

@Repository
public interface systemRolesRespository extends JpaRepository<SystemRolesEntity, String> {
    Page<SystemRolesEntity> findAll(Pageable pageable);
}
