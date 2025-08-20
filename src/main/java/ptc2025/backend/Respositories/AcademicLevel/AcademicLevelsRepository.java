package ptc2025.backend.Respositories.AcademicLevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;

@Repository
public interface AcademicLevelsRepository extends JpaRepository<AcademicLevelsEntity, String> {
}
