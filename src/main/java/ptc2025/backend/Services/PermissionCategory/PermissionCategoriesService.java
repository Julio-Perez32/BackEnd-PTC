package ptc2025.backend.Services.PermissionCategory;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.PermisionCategory.PermissionCategoriesEntity;
import ptc2025.backend.Models.DTO.PermissionCategory.PermissionCategoriesDTO;
import ptc2025.backend.Respositories.PermissionCategory.PermissionCategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PermissionCategoriesService {

    @Autowired
    PermissionCategoriesRepository repo;

    public List<PermissionCategoriesDTO> getAllPermissions(){
        List<PermissionCategoriesEntity> permissions = repo.findAll();
        return permissions.stream()
                .map(this::convertirAPermissionsDTO)
                .collect(Collectors.toList());
    }

    public PermissionCategoriesDTO convertirAPermissionsDTO(PermissionCategoriesEntity permissions){
        PermissionCategoriesDTO dto = new PermissionCategoriesDTO();
        dto.setCategoryID(permissions.getCategoryID());
        dto.setCategoryName(permissions.getCategoryName());
        return dto;
    }

    public PermissionCategoriesEntity convertirAPermissionEntity(PermissionCategoriesDTO dto){
        PermissionCategoriesEntity entity = new PermissionCategoriesEntity();
        entity.setCategoryID(dto.getCategoryID());
        entity.setCategoryName(dto.getCategoryName());
        return entity;
    }

    public PermissionCategoriesDTO insertPermission(PermissionCategoriesDTO categories){
        if(categories == null || categories.getCategoryName() == null || categories.getCategoryName().isEmpty()){
            throw new IllegalArgumentException("El permiso debe de contener un nombre.");
        }
        try {
            PermissionCategoriesEntity entity = convertirAPermissionEntity(categories);
            PermissionCategoriesEntity permissionSaved = repo.save(entity);
            return convertirAPermissionsDTO(permissionSaved);
        }catch (Exception e){
            log.error("Error al registrar la categoria: " + e.getMessage());
            throw new IllegalArgumentException("Error al registrar la categoria.");
        }
    }

    public PermissionCategoriesDTO updatePermission(String id, PermissionCategoriesDTO json){
        PermissionCategoriesEntity existsPermission = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Permiso no encontrado."));
        existsPermission.setCategoryName(json.getCategoryName());

        PermissionCategoriesEntity updatedPermission = repo.save(existsPermission);

        return convertirAPermissionsDTO(updatedPermission);
    }

    public boolean deletePermission(String id){
        try{
            PermissionCategoriesEntity existsPermission = repo.findById(id).orElse(null);
            if(existsPermission != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró permiso con ID: " + id + " para eliminar",1);
        }
    }
}
