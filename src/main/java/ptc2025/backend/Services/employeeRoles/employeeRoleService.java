package ptc2025.backend.Services.employeeRoles;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.employeeRoles.employeeRolesEntity;
import ptc2025.backend.Models.DTO.employeeRoles.employeeRolesDTO;
import ptc2025.backend.Respositories.employeeRoles.employeeRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class employeeRoleService {

    @Autowired
    private employeeRoleRepository repo;

    public List<employeeRolesDTO> getAllEmployeeRoles(){
        List<employeeRolesEntity> employiRol = repo.findAll();
        return employiRol.stream()
                .map(this::convertirEmployiRolADTO)
                .collect(Collectors.toList());
    }

    public employeeRolesDTO convertirEmployiRolADTO(employeeRolesEntity emploRol){
        employeeRolesDTO dto = new employeeRolesDTO();
        dto.setId(emploRol.getId());
        dto.setUniversityID(emploRol.getUniversityID());
        dto.setRoleName(emploRol.getRoleName());
        dto.setRoleType(emploRol.getRoleType());
        return dto;
    }

    public employeeRolesDTO insertarDatos(employeeRolesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no validos");
        }
        try{
            employeeRolesEntity entity = convertirAEntity(data);
            employeeRolesEntity employeeRoleGuardado = repo.save(entity);
            return convertirEmployiRolADTO(employeeRoleGuardado);
        }catch (Exception e){
            log.error("Error al registrar el dato" + e.getMessage());
            throw new RuntimeException("Error al ingresar el nuevo dato");
        }
    }

    private employeeRolesEntity convertirAEntity(employeeRolesDTO data) {
        employeeRolesEntity entity = new employeeRolesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setRoleName(data.getRoleName());
        entity.setRoleType(data.getRoleType());
        return entity;
    }

    public employeeRolesDTO actualizarEmployeeRoles(String id, employeeRolesDTO json) {
        employeeRolesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Registro no encontrado"));
        existente.setUniversityID(json.getUniversityID());
        existente.setRoleName(json.getRoleName());
        existente.setRoleType(json.getRoleType());
        employeeRolesEntity EmployeeRoleActualizado = repo.save(existente);
        return convertirEmployiRolADTO(EmployeeRoleActualizado);
    }

    public boolean eliminarEmployeeRole(String id) {
        try {
            employeeRolesEntity existente = repo.findById(id).orElse(null);
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
