package ptc2025.backend.Respositories.courseOfferingSchedules;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.courseOfferingSchedules.courseOfferingSchedulesEntity;

public interface courseOfferingScheduleRepository extends JpaRepository<courseOfferingSchedulesEntity, String> {
}
