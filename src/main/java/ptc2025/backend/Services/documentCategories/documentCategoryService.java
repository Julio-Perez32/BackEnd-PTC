package ptc2025.backend.Services.documentCategories;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.documentCategories.documentCategoriesEntity;
import ptc2025.backend.Models.DTO.documentCategories.documentCategoriesDTO;
import ptc2025.backend.Respositories.documentCategories.documentCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class documentCategoryService {

    @Autowired
    private documentCategoryRepository repo;

    public List<documentCategoriesDTO> getDocumentCategories() {
        List<documentCategoriesEntity> documentCtype = repo.findAll();
        return documentCtype.stream()
                .map(this::ConvertDocumentCategoriesDTO)
                .collect(Collectors.toList());
    }

    public documentCategoriesDTO ConvertDocumentCategoriesDTO(documentCategoriesEntity documentType){
        documentCategoriesDTO dto = new documentCategoriesDTO();
        dto.setId(documentType.getId());
        dto.setUniversityID(documentType.getUniversityID());
        dto.setDocumentCategory(documentType.getDocumentCategory());
        return dto;
    }
    private documentCategoriesEntity convertirAEntity(documentCategoriesDTO data) {
        documentCategoriesEntity entity = new documentCategoriesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setDocumentCategory(data.getDocumentCategory());
        return entity;
    }

    public documentCategoriesDTO insertarDatos(documentCategoriesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("No pueden haber campos nulos");
        }
        try{
            documentCategoriesEntity entity = convertirAEntity(data);
            documentCategoriesEntity documentCategorieGuardada = repo.save(entity);
            return ConvertDocumentCategoriesDTO(documentCategorieGuardada);
        }catch (Exception e){
            log.error("Error al nuevo documentcategory" + e.getMessage());
            throw new RuntimeException("Error al ingresar el nuevo documentCategory");
        }
    }


    public documentCategoriesDTO actualizarDocumentCategory(String id, documentCategoriesDTO json) {
        documentCategoriesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el DocumentCategory"));
        existente.setUniversityID(json.getUniversityID());
        existente.setDocumentCategory(json.getDocumentCategory());
        documentCategoriesEntity documentCategoryActualizada = repo.save(existente);
        return ConvertDocumentCategoriesDTO(documentCategoryActualizada);
    }

    public boolean eliminardocumentCategories(String id) {
        try{
            documentCategoriesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("no se encontro el registo", 1);
        }
    }
}
