package ptc2025.backend.Respositories.CourseOfferingSchedules;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.CourseOfferingSchedules.CourseOfferingSchedulesEntity;
@Repository
public interface CourseOfferingSchedulesRepository extends JpaRepository<CourseOfferingSchedulesEntity, String> {
    Page<CourseOfferingSchedulesEntity> findAll(Pageable pageable);
}
