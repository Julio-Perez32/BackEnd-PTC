package ptc2025.backend.Respositories.Faculties;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;

@Repository
public interface FacultiesRepository extends JpaRepository<FacultiesEntity, String> {
    Page<FacultiesEntity> findAll(Pageable pageable);
}
