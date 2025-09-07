package ptc2025.backend.Respositories.SecurityAnswers;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.SecurityAnswers.SecurityAnswersEntity;

public interface    SecurityAnswerRepository extends JpaRepository<SecurityAnswersEntity, String> {
}
