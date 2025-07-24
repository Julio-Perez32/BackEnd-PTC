package ptc2025.backend.Services.userRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.userRoles.UserRoleEntity;
import ptc2025.backend.Models.DTO.userRoles.UserRoleDTO;
import ptc2025.backend.Respositories.userRoles.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserRoleService {

    @Autowired
    private UserRoleRepository repository;

    public List<UserRoleDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public UserRoleDTO insertar(UserRoleDTO dto) {
        if (repository.existsById(dto.getId())) {
            throw new IllegalArgumentException("El rol ya existe");
        }
        UserRoleEntity entity = convertirAEntity(dto);
        return convertirADTO(repository.save(entity));
    }

    public UserRoleDTO actualizar(String id, UserRoleDTO dto) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el rol con ID: " + id);
        }
        UserRoleEntity entity = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Error interno al acceder al rol"));

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAccessLevel(dto.getAccessLevel());
        entity.setIsActive(dto.getIsActive());

        return convertirADTO(repository.save(entity));
    }

    public boolean eliminar(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private UserRoleDTO convertirADTO(UserRoleEntity entity) {
        return new UserRoleDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getAccessLevel(),
                entity.getIsActive()
        );
    }

    private UserRoleEntity convertirAEntity(UserRoleDTO dto) {
        return new UserRoleEntity(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getAccessLevel(),
                dto.getIsActive()
        );
    }
}
