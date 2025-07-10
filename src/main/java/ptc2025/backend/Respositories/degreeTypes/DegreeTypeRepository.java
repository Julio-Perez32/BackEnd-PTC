package ptc2025.backend.Respositories.degreeTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.degreeTypes.DegreeTypesEntity;

public interface DegreeTypeRepository extends JpaRepository<DegreeTypesEntity, String> {
}
