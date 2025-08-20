package ptc2025.backend.Respositories.Pensum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.Pensum.PensumEntity;

@Repository
public interface PensumRepository extends JpaRepository<PensumEntity, String> {
}
