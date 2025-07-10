package ptc2025.backend.Respositories.Requirements;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;

public interface RequirementRepository extends JpaRepository<RequirementsEntity, String> {
}
