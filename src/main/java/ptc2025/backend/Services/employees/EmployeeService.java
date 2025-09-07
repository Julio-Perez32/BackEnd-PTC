package ptc2025.backend.Services.employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.employees.EmployeeDTO;
import ptc2025.backend.Respositories.Departments.DepartmentsRepository;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    @Autowired
    private PeopleRepository repoPeople;

    @Autowired
    private DepartmentsRepository departmentsRepo;

    // GET
    public List<EmployeeDTO> getEmployees() {
        try {
            List<EmployeeEntity> employees = repo.findAll();
            if (employees.isEmpty()) {
                throw new ExceptionNoSuchElement("No existen empleados registrados.");
            }
            return employees.stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la lista de empleados: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la lista de empleados: " + e.getMessage());
        }
    }

    public Page<EmployeeDTO> getEmployeePagination(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<EmployeeEntity> pageEntity = repo.findAll(pageable);

            if (pageEntity.isEmpty()) {
                throw new ExceptionNoSuchElement("No existen empleados en la página solicitada.");
            }
            return pageEntity.map(this::convertirADTO);
        } catch (Exception e) {
            log.error("Error al obtener la paginación de empleados: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener la paginación de empleados: " + e.getMessage());
        }
    }

    // POST
    public EmployeeDTO insertEmployee(EmployeeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Empleado no puede ser nulo");
        }
        if (repo.existsById(dto.getId())) {
            throw new IllegalArgumentException("El empleado ya existe");
        }
        try {
            EmployeeEntity entity = convertirAEntity(dto);
            EmployeeEntity saved = repo.save(entity);
            return convertirADTO(saved);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al registrar empleado: {}", e.getMessage());
            throw e; // No reemplazamos, solo dejamos que se propague
        } catch (Exception e) {
            log.error("Error interno al registrar al empleado: {}", e.getMessage());
            throw new RuntimeException("Error interno a la hora de registrar al empleado: " + e.getMessage());
        }
    }

    // PUT
    public EmployeeDTO updateEmployee(String id, EmployeeDTO dto) {
        try {
            if (repo.existsById(id)) {
                EmployeeEntity entity = repo.getById(id);
                entity.setEmployeeCode(dto.getEmployeeCode());
                entity.setEmployeeDetail(dto.getEmployeeDetail());

                if (dto.getPersonID() != null) {
                    PeopleEntity person = repoPeople.findById(dto.getPersonID())
                            .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID proporcionado: " + dto.getPersonID()));
                    entity.setPeople(person);
                } else {
                    entity.setPeople(null);
                }

                if (dto.getDeparmentID() != null) {
                    DepartmentsEntity departments = departmentsRepo.findById(dto.getDeparmentID())
                            .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado con ID proporcionado: " + dto.getDeparmentID()));
                    entity.setDepartments(departments);
                } else {
                    entity.setDepartments(null);
                }

                EmployeeEntity saved = repo.save(entity);
                return convertirADTO(saved);
            }
            throw new IllegalArgumentException("El empleado con ID " + id + " no pudo ser actualizado");
        } catch (IllegalArgumentException e) {
            log.error("Error de validación al actualizar empleado: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error interno al actualizar empleado: {}", e.getMessage());
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
            log.error("Intento de eliminación fallido, empleado no encontrado: {}", e.getMessage());
            throw new EmptyResultDataAccessException("El empleado con ID " + id + " no existe", 1);
        } catch (Exception e) {
            log.error("Error interno al eliminar empleado: {}", e.getMessage());
            throw new RuntimeException("Error interno al eliminar el empleado: " + e.getMessage());
        }
    }

    // CONVERSIONES
    private EmployeeDTO convertirADTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setEmployeeCode(entity.getEmployeeCode());
        dto.setEmployeeDetail(entity.getEmployeeDetail());

        if (entity.getPeople() != null) {
            dto.setPersonName(entity.getPeople().getFirstName());
            dto.setPersonLastName(entity.getPeople().getLastName());
            dto.setPersonID(entity.getPeople().getPersonID());
        } else {
            dto.setPersonName("Sin Persona Asignada");
            dto.setPersonID(null);
        }

        if (entity.getDepartments() != null) {
            dto.setDeparmentID(entity.getDepartments().getDepartmentID());
        } else {
            dto.setDeparmentID(null);
        }

        return dto;
    }

    private EmployeeEntity convertirAEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmployeeCode(dto.getEmployeeCode());
        entity.setEmployeeDetail(dto.getEmployeeDetail());

        if (dto.getPersonID() != null) {
            PeopleEntity people = repoPeople.findById(dto.getPersonID())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + dto.getPersonID()));
            entity.setPeople(people);
        }

        if (dto.getDeparmentID() != null) {
            DepartmentsEntity departments = departmentsRepo.findById(dto.getDeparmentID())
                    .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado con ID: " + dto.getDeparmentID()));
            entity.setDepartments(departments);
        }
        return entity;
    }
}
