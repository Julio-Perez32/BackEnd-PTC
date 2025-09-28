package ptc2025.backend.Respositories.AcademicYear;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYearEntity, String> {
    Page<AcademicYearEntity> findAll(Pageable pageable);
}
