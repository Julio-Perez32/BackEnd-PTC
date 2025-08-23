package ptc2025.backend.Respositories.CourseOfferingSchedules;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.CourseOfferingSchedules.CourseOfferingSchedulesEntity;

public interface CourseOfferingSchedulesRepository extends JpaRepository<CourseOfferingSchedulesEntity, String> {
}
