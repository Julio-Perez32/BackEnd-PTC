package ptc2025.backend.Services.FacultyDeans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.FacultyDeans.FacultyDeansDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;
import ptc2025.backend.Respositories.FacultyDeans.FacultyDeansRepository;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FacultyDeansService {

    @Autowired
    private FacultyDeansRepository repo;

    @Autowired
    private FacultiesRepository facultiesRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    public List<FacultyDeansDTO> obtenerDatos() {
        List<FacultyDeansEntity> lista = repo.findAll();

        if (lista.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen decanos registrados.");
        }

        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<FacultyDeansDTO> getFacultiesDeansPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacultyDeansEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen decanos registrados en la página solicitada.");
        }

        return pageEntity.map(this::convertirADTO);
    }

    public FacultyDeansDTO insertarDatos(FacultyDeansDTO data) {
        if (data == null){
            throw new ExceptionBadRequest("Datos incorrectos. El cuerpo de la solicitud no puede estar vacío.");
        }

        try {
            FacultyDeansEntity entity = convertirAEntity(data);
            FacultyDeansEntity registroGuardado = repo.save(entity);
            return convertirADTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el nuevo dato: {}", e.getMessage());
            throw new RuntimeException("Error interno al registrar el dato.");
        }
    }

    public FacultyDeansDTO actualizarDatos(String id, FacultyDeansDTO json) {
        FacultyDeansEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));

        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());

        if (json.getFacultyID() != null) {
            FacultiesEntity faculties = facultiesRepo.findById(json.getId())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada con ID: " + json.getFacultyID()));
            existente.setFaculty(faculties);
        } else {
            existente.setFaculty(null);
        }

        if (json.getEmployees() != null) {
            EmployeeEntity employee = employeeRepo.findById(json.getId())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Empleado no encontrado con ID: " + json.getEmployeeID()));
            existente.setEmployee(employee);
        } else {
            existente.setFaculty(null);
        }

        FacultyDeansEntity registroActualizado = repo.save(existente);
        return convertirADTO(registroActualizado);
    }

    public boolean EliminarDatos(String id) {
        try {
            FacultyDeansEntity existente = repo.findById(id).orElse(null);

            if (existente == null) {
                throw new ExceptionNoSuchElement("No se encontró el registro con el ID: " + id);
            }

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar el registro con ID: " + id + ". Detalle: " + e.getMessage());
        }
    }

    private FacultyDeansDTO convertirADTO(FacultyDeansEntity facultyDeansEntity) {
        FacultyDeansDTO dto = new FacultyDeansDTO();
        dto.setId(facultyDeansEntity.getId());
        dto.setStartDate(facultyDeansEntity.getStartDate());
        dto.setEndDate(facultyDeansEntity.getEndDate());

        if (facultyDeansEntity.getFaculty() != null) {
            dto.setFaculties(facultyDeansEntity.getFaculty().getFacultyName());
            dto.setFaculties(facultyDeansEntity.getFaculty().getFacultyID());
        } else {
            dto.setFaculties("Sin Facultad Asignada");
            dto.setFaculties(null);
        }

        if (facultyDeansEntity.getEmployee() != null) {
            dto.setEmployees(facultyDeansEntity.getEmployee().getEmployeeDetail());
            dto.setEmployees(facultyDeansEntity.getEmployee().getId());
        } else {
            dto.setEmployees("Sin Empleado Asignado");
            dto.setEmployees(null);
        }
        return dto;
    }

    private FacultyDeansEntity convertirAEntity(FacultyDeansDTO data) {
        FacultyDeansEntity entity = new FacultyDeansEntity();
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());

        if (data.getFaculties() != null) {
            FacultiesEntity faculty = facultiesRepo.findById(data.getId())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada con ID: " + data.getId()));
            entity.setFaculty(faculty);
        }

        if (data.getEmployees() != null) {
            EmployeeEntity employee = employeeRepo.findById(data.getId())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Empleado no encontrado con ID: " + data.getId()));
            entity.setEmployee(employee);
        }
        return entity;
    }
}
