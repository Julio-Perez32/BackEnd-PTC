package ptc2025.backend.Respositories.EvaluationInstruments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.EvaluationInstruments.EvaluationInstrumentsEntity;

@Repository
public interface EvaluationInstrumentsRepository extends JpaRepository<EvaluationInstrumentsEntity,String> {
}
