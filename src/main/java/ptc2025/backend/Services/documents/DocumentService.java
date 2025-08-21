package ptc2025.backend.Services.documents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.documents.DocumentEntity;
import ptc2025.backend.Models.DTO.documents.DocumentDTO;
import ptc2025.backend.Respositories.documents.DocumentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    // GET
    public List<DocumentDTO> getDocuments() {
        List<DocumentEntity> docs = repository.findAll();
        return docs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // POST
    public DocumentDTO insertDocument(DocumentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Documento no puede ser nulo o vacío");
        }
        try {
            if(repository.existsById(dto.getId())){
                throw new IllegalArgumentException("El documento con ID " + dto.getId() + " ya existe");
            }
            DocumentEntity entity = convertToEntity(dto);
            DocumentEntity saved = repository.save(entity);
            return convertToDTO(saved);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear documento: " + e.getMessage());
        }
    }

    // PUT
    public DocumentDTO updateDocument(String id, DocumentDTO dto) {
        try {
            if (repository.existsById(id)) {
                DocumentEntity entity = repository.getById(id);
                entity.setName(dto.getName());
                entity.setType(dto.getType());
                entity.setUploadDate(dto.getUploadDate());
                entity.setOwnerId(dto.getOwnerId());
                entity.setIsActive(dto.getIsActive());

                DocumentEntity saved = repository.save(entity);
                return convertToDTO(saved);
            }
            throw new IllegalArgumentException("El documento con ID " + id + " no existe");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar documento: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteDocument(String id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("El documento con ID " + id + " no existe", 1);
        }
    }

    // Convertir a DTO
    private DocumentDTO convertToDTO(DocumentEntity entity) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setUploadDate(entity.getUploadDate());
        dto.setOwnerId(entity.getOwnerId());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    // Convertir a Entity
    private DocumentEntity convertToEntity(DocumentDTO dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setUploadDate(dto.getUploadDate());
        entity.setOwnerId(dto.getOwnerId());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
