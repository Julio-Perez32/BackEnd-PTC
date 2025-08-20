package ptc2025.backend.Respositories.Students;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.Students.StudentsEntity;

@Repository
public interface StudentsRepository extends JpaRepository<StudentsEntity, String> {
}
