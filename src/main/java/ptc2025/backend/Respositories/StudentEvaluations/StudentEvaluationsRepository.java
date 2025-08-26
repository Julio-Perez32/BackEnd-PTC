package ptc2025.backend.Respositories.StudentEvaluations;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;

public interface StudentEvaluationsRepository extends JpaRepository<StudentEvaluationsEntity, String> {
}
