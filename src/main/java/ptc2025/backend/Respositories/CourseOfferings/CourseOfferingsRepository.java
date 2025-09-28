package ptc2025.backend.Respositories.CourseOfferings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;

@Repository
public interface CourseOfferingsRepository extends JpaRepository<CourseOfferingsEntity, String> {
    Page<CourseOfferingsEntity> findAll(Pageable pageable);
}
