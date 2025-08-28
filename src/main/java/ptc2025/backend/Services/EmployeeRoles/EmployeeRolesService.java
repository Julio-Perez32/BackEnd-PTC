package ptc2025.backend.Services.EmployeeRoles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EmployeeRoles.EmployeeRolesEntity;
import ptc2025.backend.Models.DTO.EmployeeRoles.EmployeeRolesDTO;
import ptc2025.backend.Respositories.EmployeeRoles.EmployeeRolesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeRolesService {

    @Autowired
    private EmployeeRolesRepository repo;

    public List<EmployeeRolesDTO> getAllEmployeeRoles(){
        List<EmployeeRolesEntity> employiRol = repo.findAll();
        return employiRol.stream()
                .map(this::convertirEmployiRolADTO)
                .collect(Collectors.toList());
    }

    public Page<EmployeeRolesDTO> getEmployeeRolesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeRolesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirEmployiRolADTO);
    }

    public EmployeeRolesDTO convertirEmployiRolADTO(EmployeeRolesEntity emploRol){
        EmployeeRolesDTO dto = new EmployeeRolesDTO();
        dto.setId(emploRol.getId());
        dto.setUniversityID(emploRol.getUniversityID());
        dto.setRoleName(emploRol.getRoleName());
        dto.setRoleType(emploRol.getRoleType());
        return dto;
    }

    public EmployeeRolesDTO insertarDatos(EmployeeRolesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no validos");
        }
        try{
            EmployeeRolesEntity entity = convertirAEntity(data);
            EmployeeRolesEntity employeeRoleGuardado = repo.save(entity);
            return convertirEmployiRolADTO(employeeRoleGuardado);
        }catch (Exception e){
            log.error("Error al registrar el dato" + e.getMessage());
            throw new RuntimeException("Error al ingresar el nuevo dato");
        }
    }

    private EmployeeRolesEntity convertirAEntity(EmployeeRolesDTO data) {
        EmployeeRolesEntity entity = new EmployeeRolesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setRoleName(data.getRoleName());
        entity.setRoleType(data.getRoleType());
        return entity;
    }

    public EmployeeRolesDTO actualizarEmployeeRoles(String id, EmployeeRolesDTO json) {
        EmployeeRolesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setRoleName(json.getRoleName());
        existente.setRoleType(json.getRoleType());
        EmployeeRolesEntity EmployeeRoleActualizado = repo.save(existente);
        return convertirEmployiRolADTO(EmployeeRoleActualizado);
    }

    public boolean eliminarEmployeeRole(String id) {
        try {
            EmployeeRolesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro", 1);
        }
    }
}
