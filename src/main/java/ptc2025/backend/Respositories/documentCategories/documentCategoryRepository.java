package ptc2025.backend.Respositories.documentCategories;

import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.documentCategories.documentCategoriesEntity;

public interface documentCategoryRepository extends JpaRepository<documentCategoriesEntity, String> {
}
