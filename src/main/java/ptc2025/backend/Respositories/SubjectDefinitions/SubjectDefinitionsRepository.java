package ptc2025.backend.Respositories.SubjectDefinitions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;

@Repository
public interface SubjectDefinitionsRepository extends JpaRepository<SubjectDefinitionsEntity, String> {
    Page<SubjectDefinitionsEntity> findAll(Pageable pageable);
}
