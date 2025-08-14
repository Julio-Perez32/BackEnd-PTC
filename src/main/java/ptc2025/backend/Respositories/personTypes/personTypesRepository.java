package ptc2025.backend.Respositories.personTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.personTypes.personTypesEntity;

public interface personTypesRepository extends JpaRepository<personTypesEntity, String> {
}
