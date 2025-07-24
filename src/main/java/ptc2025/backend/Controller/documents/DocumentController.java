package ptc2025.backend.Controller.documents;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ptc2025.backend.Entities.documents.DocumentEntity;
import ptc2025.backend.Services.documents.DocumentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin
public class DocumentController {

    private final DocumentService documentService;

    //Constructor

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<DocumentEntity> getAll() {
        return documentService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<DocumentEntity> getById(@PathVariable Long id) {
        return documentService.findById(id);
    }

    @PostMapping
    public DocumentEntity create( @Valid @RequestBody DocumentEntity document) {
        return documentService.save(document);
    }

    @PutMapping("/{id}")
    public DocumentEntity update(@PathVariable Long id, @Valid @RequestBody DocumentEntity document) {
        return documentService.update(id, document);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        documentService.delete(id);
    }
}
