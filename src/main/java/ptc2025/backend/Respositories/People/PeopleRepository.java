package ptc2025.backend.Respositories.People;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.People.PeopleEntity;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleEntity, String> {
    Page<PeopleEntity> findAll(Pageable pageable);
}
