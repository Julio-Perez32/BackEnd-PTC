package ptc2025.backend.Respositories.StudentCareerEnrollments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;

@Repository
public interface StudentCareerEnrollmentsRepository extends JpaRepository<StudentCareerEnrollmentsEntity, String> {
    Page<StudentCareerEnrollmentsEntity> findAll(Pageable pageable);
}
