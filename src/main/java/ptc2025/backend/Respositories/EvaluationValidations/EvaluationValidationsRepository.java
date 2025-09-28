package ptc2025.backend.Respositories.EvaluationValidations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.EvaluationValidations.EvaluationValidationsEntity;
import ptc2025.backend.Models.DTO.EvaluationValidations.EvaluationValidationsDTO;

@Repository
public interface EvaluationValidationsRepository extends JpaRepository<EvaluationValidationsEntity, String> {
    Page<EvaluationValidationsEntity> findAll(Pageable pageable);
}
