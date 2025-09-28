package ptc2025.backend.Respositories.studentCycleEnrollments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;

@Repository
public interface StudentCycleEnrollmentRepository extends JpaRepository<StudentCycleEnrollmentEntity, String> {
    Page<StudentCycleEnrollmentEntity> findAll(Pageable pageable);
}

