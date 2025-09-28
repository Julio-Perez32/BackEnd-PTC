package ptc2025.backend.Respositories.Modalities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Modalities.ModalitiesEntity;

public interface ModalityRepository extends JpaRepository<ModalitiesEntity, String> {
    Page<ModalitiesEntity> findAll(Pageable pageable);
}
