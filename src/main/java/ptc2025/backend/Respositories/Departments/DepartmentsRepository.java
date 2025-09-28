package ptc2025.backend.Respositories.Departments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;

@Repository
public interface DepartmentsRepository extends JpaRepository<DepartmentsEntity, String> {
    Page<DepartmentsEntity> findAll(Pageable pageable);
}
