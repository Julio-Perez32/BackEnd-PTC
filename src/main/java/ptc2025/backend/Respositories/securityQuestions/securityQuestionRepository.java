package ptc2025.backend.Respositories.securityQuestions;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.securityQuestions.securityQuestionsEntity;

public interface securityQuestionRepository extends JpaRepository<securityQuestionsEntity, String> {
}
