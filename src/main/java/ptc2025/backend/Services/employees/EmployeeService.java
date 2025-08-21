package ptc2025.backend.Services.employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Models.DTO.employees.EmployeeDTO;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    // GET
    public List<EmployeeDTO> getEmployees() {
        List<EmployeeEntity> employees = repo.findAll();
        return employees.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // POST
    public EmployeeDTO insertEmployee(EmployeeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }
        if (repo.existsById(dto.getId())) {
            throw new IllegalArgumentException("El empleado ya existe");
        }
        EmployeeEntity entity = convertirAEntity(dto);
        EmployeeEntity saved = repo.save(entity);
        return convertirADTO(saved);
    }

    // PUT
    public EmployeeDTO updateEmployee(String id, EmployeeDTO dto) {
        try {
            if (repo.existsById(id)) {
                EmployeeEntity entity = repo.getById(id);
                entity.setFullName(dto.getFullName());
                entity.setEmail(dto.getEmail());
                entity.setPosition(dto.getPosition());
                entity.setPhone(dto.getPhone());
                entity.setIsActive(dto.getIsActive());
                EmployeeEntity saved = repo.save(entity);
                return convertirADTO(saved);
            }
            throw new IllegalArgumentException("El empleado con ID " + id + " no pudo ser actualizado");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el empleado: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteEmployee(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("El empleado con ID " + id + " no existe", 1);
        }
    }

    // CONVERSIONES
    private EmployeeDTO convertirADTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setPosition(entity.getPosition());
        dto.setPhone(entity.getPhone());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private EmployeeEntity convertirAEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPosition(dto.getPosition());
        entity.setPhone(dto.getPhone());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
