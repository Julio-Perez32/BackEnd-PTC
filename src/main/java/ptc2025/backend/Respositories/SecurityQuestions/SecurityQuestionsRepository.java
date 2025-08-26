package ptc2025.backend.Respositories.SecurityQuestions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.SecurityQuestions.SecurityQuestionsEntity;

@Repository
public interface SecurityQuestionsRepository extends JpaRepository<SecurityQuestionsEntity, String> {
    Page<SecurityQuestionsEntity> findAll(Pageable pageable);
}
