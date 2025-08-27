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

    // GET
    public List<DocumentDTO> getDocuments() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET DE PAGINACION
    public Page<DocumentDTO> getDocumentsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .map(this::convertToDTO);
    }


    // POST
    public DocumentDTO insertDocument(DocumentDTO dto) {
        if(dto == null || dto.getDocumentName() == null || dto.getDocumentName().isBlank()){
            throw new IllegalArgumentException("Los campos deben de estar completos");
        }
        try{
            DocumentEntity entity = convertToEntity(dto);
            DocumentEntity save = repository.save(entity);
            return convertToDTO(save);
        }catch (Exception e){
            log.error("Error al registrar el documento " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el documento");
        }
    }

    // PUT
    public DocumentDTO updateDocument(String id, DocumentDTO dto) {
        DocumentEntity exists = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Documento no encontrado"));
        exists.setName(dto.getDocumentName());

        if(dto.getDocumentCategoryID() != null){
            DocumentCategoriesEntity documentCategories = repocategoriesRepository.findById(dto.getDocumentCategoryID()).orElseThrow(
                    () -> new IllegalArgumentException("Categoria de documento no econtrado con ID " +dto.getDocumentCategoryID()));
            exists.setDocumentCategory(documentCategories);
        }else {
            exists.setDocumentCategory(null);
        }

        DocumentEntity updatedDocument = repository.save(exists);
        return convertToDTO(updatedDocument);
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
        dto.setDocumentID(entity.getId());

        if(entity.getDocumentCategory() != null){
            dto.setDocumentCategoryID(entity.getDocumentCategory().getId());
        }else {
            dto.setDocumentCategoryID( null);
        }
        return dto;
    }

    // Convertir a Entity
    private DocumentEntity convertToEntity(DocumentDTO dto) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(dto.getDocumentID());

        if (dto.getDocumentCategoryID() != null){
            DocumentCategoriesEntity documentCategories = repocategoriesRepository.findById(dto.getDocumentCategoryID()).orElseThrow(
                    () -> new IllegalArgumentException("Categoria de documento no encontrado con ID " + dto.getDocumentCategoryID()));
            entity.setDocumentCategory(documentCategories);
        }
        return entity;
    }
}
