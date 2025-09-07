package ptc2025.backend.Services.Documents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.Documents.DocumentEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.Documents.DocumentDTO;
import ptc2025.backend.Respositories.DocumentCategories.DocumentCategoriesRepository;
import ptc2025.backend.Respositories.Documents.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentCategoriesRepository repocategoriesRepository;

    public List<DocumentDTO> getDocuments() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<DocumentDTO> getDocumentsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public DocumentDTO insertDocument(DocumentDTO dto) {
        if (dto == null || dto.getDocumentName() == null || dto.getDocumentName().isBlank()) {
            throw new ExceptionBadRequest("Los campos no pueden estar vacíos o nulos");
        }

        try {
            DocumentEntity entity = convertToEntity(dto);
            DocumentEntity savedDocument = repository.save(entity);
            return convertToDTO(savedDocument);
        } catch (Exception e) {
            log.error("Error al registrar el documento: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el documento");
        }
    }

    public DocumentDTO updateDocument(String id, DocumentDTO dto) {
        DocumentEntity exists = repository.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Documento no encontrado con ID: " + id));

        exists.setName(dto.getDocumentName());

        if (dto.getDocumentCategoryID() != null) {
            DocumentCategoriesEntity documentCategories = repocategoriesRepository.findById(dto.getDocumentCategoryID())
                    .orElseThrow(() -> new ExceptionNotFound("Categoría de documento no encontrada con ID: " + dto.getDocumentCategoryID()));
            exists.setDocumentCategory(documentCategories);
        } else {
            exists.setDocumentCategory(null);
        }

        try {
            DocumentEntity updatedDocument = repository.save(exists);
            return convertToDTO(updatedDocument);
        } catch (Exception e) {
            log.error("Error al actualizar el documento: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al actualizar el documento");
        }
    }

    public boolean deleteDocument(String id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            throw new ExceptionNotFound("El documento con ID " + id + " no existe");
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServerError("Error al intentar eliminar el documento: " + e.getMessage());
        }
    }

    private DocumentDTO convertToDTO(DocumentEntity entity) {
        DocumentDTO dto = new DocumentDTO();
        dto.setDocumentID(entity.getId());
        dto.setDocumentName(entity.getName());

        if (entity.getDocumentCategory() != null) {
            dto.setDocumentCategoryID(entity.getDocumentCategory().getId());
            dto.setDocumentCategory(entity.getDocumentCategory().getDocumentCategory());
        } else {
            dto.setDocumentCategoryID(null);
            dto.setDocumentCategory("Sin categoría asignada");
        }
        return dto;
    }

    private DocumentEntity convertToEntity(DocumentDTO dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getDocumentID());
        entity.setName(dto.getDocumentName());

        if (dto.getDocumentCategoryID() != null) {
            DocumentCategoriesEntity documentCategories = repocategoriesRepository.findById(dto.getDocumentCategoryID())
                    .orElseThrow(() -> new ExceptionNotFound("Categoría de documento no encontrada con ID: " + dto.getDocumentCategoryID()));
            entity.setDocumentCategory(documentCategories);
        } else {
            entity.setDocumentCategory(null);
        }

        return entity;
    }
}
