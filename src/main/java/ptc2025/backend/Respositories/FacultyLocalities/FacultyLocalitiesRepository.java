package ptc2025.backend.Respositories.FacultyLocalities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.FacultyLocalities.FacultyLocalitiesEntity;

@Repository
public interface FacultyLocalitiesRepository extends JpaRepository<FacultyLocalitiesEntity, String> {
}
