package ptc2025.backend.Respositories.DegreeTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;

public interface DegreeTypesRepository extends JpaRepository<DegreeTypesEntity, String> {
}
