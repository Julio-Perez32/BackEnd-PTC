package ptc2025.backend.Respositories.facultyCorrelatives;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;

public interface facultyCorrelativesRespository extends JpaRepository<facultyCorrelativesEntity, String> {
    Page<facultyCorrelativesEntity> findAll(Pageable pageable);
}
