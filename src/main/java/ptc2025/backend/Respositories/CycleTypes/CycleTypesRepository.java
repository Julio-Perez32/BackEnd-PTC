package ptc2025.backend.Respositories.CycleTypes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;

@Repository
public interface CycleTypesRepository extends JpaRepository<CycleTypesEntity, String> {
    Page<CycleTypesEntity> findAll(Pageable pageable);
}

