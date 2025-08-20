package ptc2025.backend.Respositories.PensumSubject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.PensumSubject.PensumSubjectEntity;

@Repository
public interface PensumSubjectRepository extends JpaRepository<PensumSubjectEntity, String> {
}
