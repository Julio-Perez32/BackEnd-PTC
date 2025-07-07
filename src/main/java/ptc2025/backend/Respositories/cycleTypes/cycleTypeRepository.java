package ptc2025.backend.Respositories.cycleTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.cycleTypes.cycleTypesEntity;

public interface cycleTypeRepository extends JpaRepository<cycleTypesEntity, String> {
}

