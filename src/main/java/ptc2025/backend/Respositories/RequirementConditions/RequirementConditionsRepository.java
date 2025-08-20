package ptc2025.backend.Respositories.RequirementConditions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Entities.RequirementConditions.RequirementConditionsEntity;

@Repository
public interface RequirementConditionsRepository extends JpaRepository<RequirementConditionsEntity, String> {
}
