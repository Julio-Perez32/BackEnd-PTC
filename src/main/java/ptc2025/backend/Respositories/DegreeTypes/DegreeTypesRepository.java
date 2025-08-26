package ptc2025.backend.Respositories.DegreeTypes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;

@Repository
public interface DegreeTypesRepository extends JpaRepository<DegreeTypesEntity, String> {

    Page<DegreeTypesEntity> findAll(Pageable pageable);
}
