package ptc2025.backend.Services.DocumentCategories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DocumentCategories.DocumentCategoriesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.DocumentCategories.DocumentCategoriesDTO;
import ptc2025.backend.Respositories.DocumentCategories.DocumentCategoriesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentCategoriesService {

    @Autowired
    private DocumentCategoriesRepository repo;

    @Autowired
    private UniversityRespository repoUniversity;

    public List<DocumentCategoriesDTO> getDocumentCategories() {
        List<DocumentCategoriesEntity> documentCtype = repo.findAll();
        return documentCtype.stream()
                .map(this::ConvertDocumentCategoriesDTO)
                .collect(Collectors.toList());
    }

    public Page<DocumentCategoriesDTO> getDocumentCategoriesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentCategoriesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertDocumentCategoriesDTO);
    }

    public DocumentCategoriesDTO ConvertDocumentCategoriesDTO(DocumentCategoriesEntity documentType){
        DocumentCategoriesDTO dto = new DocumentCategoriesDTO();
        dto.setId(documentType.getId());
        dto.setDocumentCategory(documentType.getDocumentCategory());

        if (documentType.getUniversity() != null) {
            dto.setUniversityName(documentType.getUniversity().getUniversityName());
            dto.setUniversityID(documentType.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    private DocumentCategoriesEntity convertirAEntity(DocumentCategoriesDTO data) {
        DocumentCategoriesEntity entity = new DocumentCategoriesEntity();
        entity.setDocumentCategory(data.getDocumentCategory());

        if (data.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new ExceptionNotFound("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }

    public DocumentCategoriesDTO insertarDatos(DocumentCategoriesDTO data) {
        if (data == null || data.getDocumentCategory() == null || data.getDocumentCategory().isEmpty()) {
            throw new ExceptionBadRequest("No pueden haber campos nulos o vacíos");
        }
        try {
            DocumentCategoriesEntity entity = convertirAEntity(data);
            DocumentCategoriesEntity documentCategorieGuardada = repo.save(entity);
            return ConvertDocumentCategoriesDTO(documentCategorieGuardada);
        } catch (Exception e) {
            log.error("Error al registrar nueva DocumentCategory: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al registrar la categoría de documento: " + e.getMessage());
        }
    }

    public DocumentCategoriesDTO actualizarDocumentCategory(String id, DocumentCategoriesDTO json) {
        DocumentCategoriesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("No se encontró la categoría de documento con ID: " + id));

        existente.setDocumentCategory(json.getDocumentCategory());

        if (json.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new ExceptionNotFound("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }

        DocumentCategoriesEntity documentCategoryActualizada = repo.save(existente);
        return ConvertDocumentCategoriesDTO(documentCategoryActualizada);
    }

    public boolean eliminardocumentCategories(String id) {
        try {
            DocumentCategoriesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("No se encontró la categoría de documento con ID: " + id));

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServerError("Error al intentar eliminar la categoría de documento: " + e.getMessage());
        }
    }
}
