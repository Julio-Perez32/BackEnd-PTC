package ptc2025.backend.Respositories.EntityType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.EntityType.EntityTypesEntity;

public interface EntityTypesRepository extends JpaRepository <EntityTypesEntity, String> {
    Page<EntityTypesEntity> findAll(Pageable pageable);
}
