package ptc2025.backend.Respositories.FacultyDeans;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;

@Repository
public interface FacultyDeansRepository extends JpaRepository<FacultyDeansEntity, String> {
    Page<FacultyDeansEntity> findAll(Pageable pageable);
}
