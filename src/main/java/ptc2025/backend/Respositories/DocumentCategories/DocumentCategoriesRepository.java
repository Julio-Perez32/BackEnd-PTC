package ptc2025.backend.Respositories.DocumentCategories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
@Repository
public interface DocumentCategoriesRepository extends JpaRepository<DocumentCategoriesEntity, String> {
    Page<DocumentCategoriesEntity> findAll(Pageable pageable);
}
