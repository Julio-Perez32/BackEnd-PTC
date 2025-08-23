package ptc2025.backend.Respositories.CycleTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;

public interface CycleTypesRepository extends JpaRepository<CycleTypesEntity, String> {
}

