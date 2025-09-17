package ptc2025.backend.Respositories.Students;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<StudentsEntity, String> {
    boolean existsByPeople(PeopleEntity people);
}
