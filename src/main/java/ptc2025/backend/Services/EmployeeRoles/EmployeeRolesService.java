package ptc2025.backend.Services.EmployeeRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EmployeeRoles.EmployeeRolesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.EmployeeRoles.EmployeeRolesDTO;
import ptc2025.backend.Respositories.EmployeeRoles.EmployeeRolesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeRolesService {

    @Autowired
    private EmployeeRolesRepository repo;

    // GET - Listar todos los roles
    public List<EmployeeRolesDTO> getAllEmployeeRoles() {
        List<EmployeeRolesEntity> employiRol = repo.findAll();
        return employiRol.stream()
                .map(this::convertirEmployiRolADTO)
                .collect(Collectors.toList());
    }

    // GET PAGINADO
    public Page<EmployeeRolesDTO> getEmployeeRolesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeRolesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirEmployiRolADTO);
    }

    // Convertir Entity -> DTO
    public EmployeeRolesDTO convertirEmployiRolADTO(EmployeeRolesEntity emploRol) {
        EmployeeRolesDTO dto = new EmployeeRolesDTO();
        dto.setId(emploRol.getId());
        dto.setUniversityID(emploRol.getUniversityID());
        dto.setRoleName(emploRol.getRoleName());
        dto.setRoleType(emploRol.getRoleType());
        return dto;
    }

    // POST - Insertar nuevo rol
    public EmployeeRolesDTO insertarDatos(EmployeeRolesDTO data) {
        if (data == null) {
            throw new ExceptionBadRequest("Datos no válidos para la inserción");
        }
        try {
            EmployeeRolesEntity entity = convertirAEntity(data);
            EmployeeRolesEntity employeeRoleGuardado = repo.save(entity);
            return convertirEmployiRolADTO(employeeRoleGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el dato: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el nuevo rol");
        }
    }

    // Convertir DTO -> Entity
    private EmployeeRolesEntity convertirAEntity(EmployeeRolesDTO data) {
        EmployeeRolesEntity entity = new EmployeeRolesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setRoleName(data.getRoleName());
        entity.setRoleType(data.getRoleType());
        return entity;
    }

    // PUT - Actualizar rol
    public EmployeeRolesDTO actualizarEmployeeRoles(String id, EmployeeRolesDTO json) {
        EmployeeRolesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Registro no encontrado con ID: " + id));

        existente.setUniversityID(json.getUniversityID());
        existente.setRoleName(json.getRoleName());
        existente.setRoleType(json.getRoleType());

        try {
            EmployeeRolesEntity employeeRoleActualizado = repo.save(existente);
            return convertirEmployiRolADTO(employeeRoleActualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el registro con ID {}: {}", id, e.getMessage());
            throw new ExceptionServerError("Error interno al actualizar el rol");
        }
    }

    // DELETE - Eliminar rol
    public boolean eliminarEmployeeRole(String id) {
        try {
            EmployeeRolesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("No se encontró el registro con ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServerError("Error al intentar eliminar el registro: " + e.getMessage());
        }
    }
}
