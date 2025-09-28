package ptc2025.backend.Respositories.StudentDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.StudentDocument.StudentDocumentsEntity;
@Repository
public interface StudentDocumentsRepository extends JpaRepository<StudentDocumentsEntity, String> {
    Page<StudentDocumentsEntity> findAll(Pageable pageable);
}
