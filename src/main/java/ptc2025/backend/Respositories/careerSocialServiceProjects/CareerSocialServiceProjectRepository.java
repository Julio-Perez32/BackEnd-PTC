package ptc2025.backend.Respositories.careerSocialServiceProjects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.careerSocialServiceProjects.CareerSocialServiceProjectEntity;

public interface CareerSocialServiceProjectRepository extends JpaRepository<CareerSocialServiceProjectEntity, String> {
    Page<CareerSocialServiceProjectEntity> findAll(Pageable pageable);
}
