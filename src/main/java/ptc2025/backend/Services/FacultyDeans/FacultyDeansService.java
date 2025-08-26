package ptc2025.backend.Services.FacultyDeans;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
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
        List<FacultyDeansEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private FacultyDeansDTO convertirADTO(FacultyDeansEntity facultyDeansEntity) {
        FacultyDeansDTO dto = new FacultyDeansDTO();
        dto.setId(facultyDeansEntity.getId());
        dto.setStartDate(facultyDeansEntity.getStartDate());
        dto.setEndDate(facultyDeansEntity.getEndDate());
        if(facultyDeansEntity.getFaculty() != null){
            dto.setFaculties(facultyDeansEntity.getFaculty().getFacultyName());
            dto.setFaculties(facultyDeansEntity.getFaculty().getFacultyID());
        }else {
            dto.setFaculties("Sin Facultad Asignada");
            dto.setFaculties(null);
        }

        if(facultyDeansEntity.getEmployee() != null){
            dto.setEmployees(facultyDeansEntity.getEmployee().getEmployeeDetail());
            dto.setEmployees(facultyDeansEntity.getEmployee().getId());
        }else {
            dto.setEmployees("Sin Empleado Asignado");
            dto.setEmployees(null);
        }
        return dto;
    }

    public FacultyDeansDTO insertarDatos(FacultyDeansDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos incorrectos");
        }
        try {
            FacultyDeansEntity entity = convertirAEntity(data);
            FacultyDeansEntity registrosGuardado = repo.save(entity);
            return convertirADTO(registrosGuardado);
        }catch (Exception e){
            log.error("Error al registrar el nuevo dato" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el dato");
        }
    }

    private FacultyDeansEntity convertirAEntity(FacultyDeansDTO data) {
        FacultyDeansEntity entity = new FacultyDeansEntity();
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());
        if(data.getFaculties() != null){
            FacultiesEntity Faculty = facultiesRepo.findById(data.getId())
                    .orElseThrow(() -> new IllegalArgumentException("facultad no encontrada con ID: " + data.getId()));
            entity.setFaculty(Faculty);
        }

        if(data.getEmployees() != null){
            EmployeeEntity employee = employeeRepo.findById(data.getId())
                    .orElseThrow(() -> new IllegalArgumentException("empleado no encontrado con ID: " + data.getId()));
            entity.setEmployee(employee);
        }
        return entity;
    }

    public FacultyDeansDTO actualizarDatos(String id, FacultyDeansDTO json) {
        FacultyDeansEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());
        if(json.getFacultyID() != null){
            FacultiesEntity faculties = facultiesRepo.findById(json.getId())
                    .orElseThrow(() -> new IllegalArgumentException("facultad no encontrada con ID: " + json.getFaculties()));
            existente.setFaculty(faculties);
        }else {
            existente.setFaculty(null);
        }

        if(json.getEmployees() != null){
            EmployeeEntity employee = employeeRepo.findById(json.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + json.getEmployeeID()));
            existente.setEmployee(employee);
        }else {
            existente.setFaculty(null);
        }
        FacultyDeansEntity RegistroActualizado = repo.save(existente);
        return convertirADTO(RegistroActualizado);
    }

    public boolean EliminarDatos(String id) {
        try {
            FacultyDeansEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro con el id" + id, 1);
        }
    }
}
