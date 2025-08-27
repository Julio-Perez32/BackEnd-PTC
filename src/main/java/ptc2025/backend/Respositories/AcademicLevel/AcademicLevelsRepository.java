package ptc2025.backend.Respositories.AcademicLevel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;



@Repository
public interface AcademicLevelsRepository extends JpaRepository<AcademicLevelsEntity, String> {
    Page<AcademicLevelsEntity> findAll(Pageable pageable);
}
