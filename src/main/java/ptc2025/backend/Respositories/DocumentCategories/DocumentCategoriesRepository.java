package ptc2025.backend.Respositories.DocumentCategories;


import org.springframework.data.jpa.repository.JpaRepository;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;

public interface DocumentCategoriesRepository extends JpaRepository<DocumentCategoriesEntity, String> {
}
