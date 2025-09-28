package ptc2025.backend.Respositories.careerCycleAvailability;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.careerCycleAvailability.CareerCycleAvailabilityEntity;

@Repository
public interface CareerCycleAvailabilityRepository extends JpaRepository<CareerCycleAvailabilityEntity, String> {
    Page<CareerCycleAvailabilityEntity> findAll(Pageable pageable);
}
