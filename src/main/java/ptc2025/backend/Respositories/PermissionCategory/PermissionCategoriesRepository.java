package ptc2025.backend.Respositories.PermissionCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.PermisionCategory.PermissionCategoriesEntity;

@Repository
public interface PermissionCategoriesRepository extends JpaRepository<PermissionCategoriesEntity, String> {
    Page<PermissionCategoriesEntity> findAll(Pageable pageable);
}
