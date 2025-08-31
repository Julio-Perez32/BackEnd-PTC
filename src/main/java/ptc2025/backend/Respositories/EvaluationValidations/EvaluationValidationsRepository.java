package ptc2025.backend.Respositories.EvaluationValidations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Models.DTO.EvaluationValidations.EvaluationValidationsDTO;

@Repository
public interface EvaluationInstrumentsRepository extends JpaRepository<EvaluationValidationsDTO, String> {
}
