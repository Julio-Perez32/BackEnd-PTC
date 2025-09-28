package ptc2025.backend.Respositories.SubjectFamilies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SubjectFamilies.SubjectFamiliesEntity;

@Repository
public interface SubjectFamiliesRepository extends JpaRepository<SubjectFamiliesEntity, String> {
    Page<SubjectFamiliesEntity> findAll(Pageable pageable);
}
