package ptc2025.backend.Respositories.CourseOfferingsTeachers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.CourseOfferingsTeachers.CourseOfferingsTeachersEntity;

@Repository
public interface CourseOfferingsTeachersRepository extends JpaRepository<CourseOfferingsTeachersEntity, String> {
}
