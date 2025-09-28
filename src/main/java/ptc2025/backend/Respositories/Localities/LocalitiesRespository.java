package ptc2025.backend.Respositories.Localities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;

@Repository
public interface LocalitiesRespository extends JpaRepository <LocalitiesEntity, String> {
    Page<LocalitiesEntity> findAll(Pageable pageable);
}
