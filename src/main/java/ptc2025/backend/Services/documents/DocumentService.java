package ptc2025.backend.Services.documents;

import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.documents.DocumentEntity;
import ptc2025.backend.Respositories.documents.DocumentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    public Optional<DocumentEntity> findById(Long id) {
        return documentRepository.findById(id);
    }

    public DocumentEntity save(DocumentEntity document) {
        return documentRepository.save(document);
    }

    public DocumentEntity update(Long id, DocumentEntity newDocument) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setType(newDocument.getType());
                    document.setNumber(newDocument.getNumber());
                    document.setIssueDate(newDocument.getIssueDate());
                    document.setCountry(newDocument.getCountry());
                    return documentRepository.save(document);
                }).orElse(null);
    }

    public void delete(Long id) {
        documentRepository.deleteById(id);
    }
}
