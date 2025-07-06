package ptc2025.backend.Respositories.Universities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.Universities.UniversityEntity;

import java.util.UUID;


@Repository
public interface UniversityRespository extends JpaRepository<UniversityEntity, String> {
}
