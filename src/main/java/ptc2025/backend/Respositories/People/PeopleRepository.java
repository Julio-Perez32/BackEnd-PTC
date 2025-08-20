package ptc2025.backend.Respositories.People;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.People.PeopleEntity;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, String> {
}
