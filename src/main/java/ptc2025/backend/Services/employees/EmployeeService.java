package ptc2025.backend.Services.employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Models.DTO.employees.EmployeeDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Autowired//inyectando repositorio
    private PeopleRepository repoPeople;

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
        try{
            EmployeeEntity entity = convertirAEntity(dto);
            EmployeeEntity saved = repo.save(entity);
            return convertirADTO(saved);
        }catch (Exception e){
            log.error("Error al registrar al empleado" + e.getMessage());
            throw new RuntimeException("Error interno a la hora de registrar al empleado");
        }

    }

    // PUT
    public EmployeeDTO updateEmployee(String id, EmployeeDTO dto) {
        try {
            if (repo.existsById(id)) {
                EmployeeEntity entity = repo.getById(id);
                entity.setEmployeeCode(dto.getEmployeeCode());
                entity.setEmployeeDetail(dto.getEmployeeDetail());
                if (dto.getPersonID() != null){
                    PeopleEntity person = repoPeople.findById(dto.getPersonID())
                            .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado con ID proporcionado: " + dto.getPersonID()));
                    entity.setPeople(person);
                }else {
                    entity.setPeople(null);
                }
                EmployeeEntity saved = repo.save(entity);
                return convertirADTO(saved);
            }
            throw new IllegalArgumentException("El empleado con ID " + id + " no pudo ser actualizado");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el empleado: " + e.getMessage());
        } //se le puso un catch para que funcione
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
        dto.setEmployeeCode(entity.getEmployeeCode());
        dto.setEmployeeDetail(entity.getEmployeeDetail());
        if(entity.getPeople() != null){
            dto.setPersonName(entity.getPeople().getFirstName());
            dto.setPersonLastName(entity.getPeople().getLastName());
            dto.setPersonID(entity.getPeople().getPersonID());
        }else {
            dto.setPersonName("Sin Persona Asignada");
            dto.setPersonID(null);
        }
        return dto;
    }

    private EmployeeEntity convertirAEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeDetail(dto.getEmployeeDetail());
        if(dto.getPersonID() != null){
            PeopleEntity people = repoPeople.findById(dto.getPersonID())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + dto.getPersonID()));
            entity.setPeople(people);
        }
        return entity;
    }
}
