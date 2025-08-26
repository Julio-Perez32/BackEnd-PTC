package ptc2025.backend.Respositories.Documents;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.Documents.DocumentEntity;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {
    Page<DocumentEntity> findAll(Pageable pageable);
}
