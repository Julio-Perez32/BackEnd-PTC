package ptc2025.backend.Respositories.SocialService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.SocialServiceProjects.SocialServiceProjectsEntity;

@Repository
public interface SocialServiceRespository extends JpaRepository <SocialServiceProjectsEntity, String> {
    Page<SocialServiceProjectsEntity> findAll(Pageable pageable);
}
