package ptc2025.backend.Services.Documents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.Documents.DocumentEntity;
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
    private DocumentCategoriesRepository categoriesRepository;

    // GET
    public List<DocumentDTO> getDocuments() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // POST
    public DocumentDTO insertDocument(DocumentDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Documento no puede ser nulo o vacío");
        }

        if (repository.existsById(dto.getId())) {
            throw new IllegalArgumentException("El documento con ID " + dto.getId() + " ya existe");
        }

        DocumentCategoriesEntity category = categoriesRepository.findById(dto.getCategoriesId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría con ID " + dto.getCategoriesId() + " no existe"));

        DocumentEntity entity = convertToEntity(dto);
        entity.setCategories(category);

        DocumentEntity saved = repository.save(entity);
        return convertToDTO(saved);
    }

    // PUT
    public DocumentDTO updateDocument(String id, DocumentDTO dto) {
        return repository.findById(id).map(entity -> {
            entity.setName(dto.getName());
            entity.setType(dto.getType());
            entity.setUploadDate(dto.getUploadDate());
            entity.setOwnerId(dto.getOwnerId());
            entity.setIsActive(dto.getIsActive());

            DocumentCategoriesEntity category = categoriesRepository.findById(dto.getCategoriesId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría con ID " + dto.getCategoriesId() + " no existe"));
            entity.setCategories(category);

            return convertToDTO(repository.save(entity));
        }).orElseThrow(() -> new IllegalArgumentException("El documento con ID " + id + " no existe"));
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
        dto.setCategoriesId(entity.getCategories().getId());
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
