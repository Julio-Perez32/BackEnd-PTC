package ptc2025.backend.Respositories.YearCycles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;

@Repository
public interface YearCyclesRepository extends JpaRepository<YearCyclesEntity, String> {
}
