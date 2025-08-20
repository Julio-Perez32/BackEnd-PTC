package ptc2025.backend.Respositories.SubjectTeachers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;

@Repository
public interface SubjectTeachersRepository extends JpaRepository<SubjectTeachersEntity, String> {
}
