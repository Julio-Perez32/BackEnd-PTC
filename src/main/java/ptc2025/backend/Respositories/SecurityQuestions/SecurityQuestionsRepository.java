package ptc2025.backend.Respositories.SecurityQuestions;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.SecurityQuestions.SecurityQuestionsEntity;

public interface SecurityQuestionsRepository extends JpaRepository<SecurityQuestionsEntity, String> {
}
