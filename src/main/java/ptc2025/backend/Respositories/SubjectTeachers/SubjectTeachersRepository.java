package ptc2025.backend.Respositories.SubjectTeachers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;

@Repository
public interface SubjectTeachersRepository extends JpaRepository<SubjectTeachersEntity, String> {
    Page<SubjectTeachersEntity> findAll(Pageable pageable);
}
