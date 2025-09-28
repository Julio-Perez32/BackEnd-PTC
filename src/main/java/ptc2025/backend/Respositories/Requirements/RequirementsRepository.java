package ptc2025.backend.Respositories.Requirements;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;

public interface RequirementsRepository extends JpaRepository<RequirementsEntity, String> {
    Page<RequirementsEntity> findAll(Pageable pageable);
}
