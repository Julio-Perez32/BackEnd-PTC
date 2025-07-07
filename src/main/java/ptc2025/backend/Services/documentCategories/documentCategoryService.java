package ptc2025.backend.Services.documentCategories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.documentCategories.documentCategoriesEntity;
import ptc2025.backend.Models.DTO.documentCategories.documentCategoriesDTO;
import ptc2025.backend.Respositories.documentCategories.documentCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

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
}
