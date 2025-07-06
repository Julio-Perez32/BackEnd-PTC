package ptc2025.backend.Controller.documentCategories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptc2025.backend.Models.DTO.documentCategories.documentCategoriesDTO;
import ptc2025.backend.Services.documentCategories.documentCategoryService;

import java.util.List;

@RestController
@RequestMapping("/documentCategories")
public class documentCategoriesController {

    @Autowired
    private documentCategoryService services;

    @GetMapping("/getAllDocumentCategories")
    public List<documentCategoriesDTO> getData() { return services.getDocumentCategories(); }
}
