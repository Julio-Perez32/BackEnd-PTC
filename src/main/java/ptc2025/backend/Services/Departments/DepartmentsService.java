package ptc2025.backend.Services.Departments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Departments.DepartmentsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.Departments.DepartmentsDTO;
import ptc2025.backend.Respositories.Departments.DepartmentsRepository;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartmentsService {

    @Autowired
    DepartmentsRepository repo;

    @Autowired
    FacultiesRepository repoFaculties;

    public List<DepartmentsDTO> getAllDepartments() {
        List<DepartmentsEntity> departments = repo.findAll();
        return departments.stream()
                .map(this::convertToDepartmentsDTO)
                .collect(Collectors.toList());
    }

    public Page<DepartmentsDTO> getDepartmentsPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DepartmentsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToDepartmentsDTO);
    }

    private DepartmentsDTO convertToDepartmentsDTO(DepartmentsEntity departments) {
        DepartmentsDTO dto = new DepartmentsDTO();
        dto.setDepartmentID(departments.getDepartmentID());
        dto.setDepartmentName(departments.getDepartmentName());
        dto.setDepartmentType(departments.getDepartmentType());

        if (departments.getFaculty() != null) {
            dto.setFacultyID(departments.getFaculty().getFacultyID());
        } else {
            dto.setFacultyID(null);
        }
        return dto;
    }

    private DepartmentsEntity convertToDepartmentEntity(DepartmentsDTO dto) {
        DepartmentsEntity entity = new DepartmentsEntity();
        entity.setDepartmentID(dto.getDepartmentID());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setDepartmentType(dto.getDepartmentType());

        if (dto.getFacultyID() != null) {
            FacultiesEntity faculties = repoFaculties.findById(dto.getFacultyID())
                    .orElseThrow(() -> new ExceptionNotFound("Facultad no encontrada con ID: " + dto.getFacultyID()));
            entity.setFaculty(faculties);
        }
        return entity;
    }

    public DepartmentsDTO insertDepartment(DepartmentsDTO dto) {
        if (dto == null ||
                dto.getFacultyID() == null || dto.getFacultyID().isEmpty() ||
                dto.getDepartmentName() == null || dto.getDepartmentName().isEmpty() ||
                dto.getDepartmentType() == null || dto.getDepartmentType().isEmpty()) {
            throw new ExceptionBadRequest("Todos los campos deben de estar llenos.");
        }
        try {
            DepartmentsEntity entity = convertToDepartmentEntity(dto);
            DepartmentsEntity departmentSaved = repo.save(entity);
            return convertToDepartmentsDTO(departmentSaved);
        } catch (Exception e) {
            log.error("Error al registrar el departamento: {}", e.getMessage());
            throw new ExceptionServerError("Error interno al registrar el departamento: " + e.getMessage());
        }
    }

    public DepartmentsDTO updateDepartment(String id, DepartmentsDTO json) {
        DepartmentsEntity existsDepartment = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Departamento no encontrado con ID: " + id));

        existsDepartment.setDepartmentName(json.getDepartmentName());
        existsDepartment.setDepartmentType(json.getDepartmentType());

        if (json.getFacultyID() != null) {
            FacultiesEntity faculties = repoFaculties.findById(json.getFacultyID())
                    .orElseThrow(() -> new ExceptionNotFound("Facultad no encontrada con ID: " + json.getFacultyID()));
            existsDepartment.setFaculty(faculties);
        } else {
            existsDepartment.setFaculty(null);
        }

        DepartmentsEntity departmentUpdated = repo.save(existsDepartment);
        return convertToDepartmentsDTO(departmentUpdated);
    }

    public boolean deleteDepartment(String id) {
        try {
            DepartmentsEntity existsDepartment = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNotFound("Departamento no encontrado con ID: " + id));

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServerError("Error al intentar eliminar el departamento: " + e.getMessage());
        }
    }
}
