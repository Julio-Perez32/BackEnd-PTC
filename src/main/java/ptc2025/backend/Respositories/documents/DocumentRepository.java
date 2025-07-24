package ptc2025.backend.Respositories.documents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.documents.DocumentEntity;

import javax.swing.text.Document;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

}
