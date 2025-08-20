package ptc2025.backend.Respositories.Faculties;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;

@Repository
public interface FacultiesRepository extends JpaRepository<FacultiesEntity, String> {
}
