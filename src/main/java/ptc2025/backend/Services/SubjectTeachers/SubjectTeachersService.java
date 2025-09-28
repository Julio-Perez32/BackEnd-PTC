package ptc2025.backend.Services.SubjectTeachers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.SubjectDefinitions.SubjectDefinitionsEntity;
import ptc2025.backend.Entities.SubjectTeachers.SubjectTeachersEntity;
import ptc2025.backend.Entities.employees.EmployeeEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.SubjectDefinitions.SubjectDefinitionsDTO;
import ptc2025.backend.Models.DTO.SubjectTeachers.SubjectTeachersDTO;
import ptc2025.backend.Respositories.SubjectDefinitions.SubjectDefinitionsRepository;
import ptc2025.backend.Respositories.SubjectTeachers.SubjectTeachersRepository;
import ptc2025.backend.Respositories.employees.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubjectTeachersService {

    @Autowired
    SubjectTeachersRepository repo;

    @Autowired
    SubjectDefinitionsRepository repoSubjectDefinitions;

    @Autowired
    EmployeeRepository repoEmployee;

    public SubjectTeachersDTO convertToSubjectTeachersDTO(SubjectTeachersEntity entity) {
        SubjectTeachersDTO dto = new SubjectTeachersDTO();
        dto.setSubjectTeacherID(entity.getSubjectTeacherID());

        if (entity.getEmployee() != null) {
            dto.setEmployeeID(entity.getEmployee().getId());
        } else {
            dto.setEmployeeID(null);
        }

        if (entity.getSubjectDefinitions() != null) {
            dto.setSubjectID(entity.getSubjectDefinitions().getSubjectID());
        } else {
            dto.setSubjectID(null);
        }

        return dto;
    }

    public SubjectTeachersEntity convertToSubjectEntity(SubjectTeachersDTO dto) {
        SubjectTeachersEntity entity = new SubjectTeachersEntity();
        entity.setSubjectTeacherID(dto.getSubjectTeacherID());

        if (dto.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(dto.getSubjectID())
                    .orElseThrow(() -> new ExceptionNotFound("Materia no encontrada con ID " + dto.getSubjectID()));
            entity.setSubjectDefinitions(subjectDefinitions);
        }

        if (dto.getEmployeeID() != null) {
            EmployeeEntity employee = repoEmployee.findById(dto.getEmployeeID())
                    .orElseThrow(() -> new ExceptionNotFound("Empleado no encontrado con ID " + dto.getEmployeeID()));
            entity.setEmployee(employee);
        }
        return entity;
    }

    public List<SubjectTeachersDTO> getAllSubjectTeachers() {
        try {
            List<SubjectTeachersEntity> subTeachers = repo.findAll();
            return subTeachers.stream()
                    .map(this::convertToSubjectTeachersDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la lista de docentes", e);
            throw new ExceptionServerError("Error interno al obtener la lista de docentes");
        }
    }
    public Page<SubjectTeachersDTO> getAllSubjectTeachersPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubjectTeachersEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron definiciones de materias para la página solicitada.");
        }

        return pageEntity.map(this::convertToSubjectTeachersDTO);
    }


    public SubjectTeachersDTO insertSubjectTeacher(SubjectTeachersDTO dto) {
        if (dto == null || dto.getSubjectID() == null || dto.getSubjectID().isBlank() ||
                dto.getEmployeeID() == null || dto.getEmployeeID().isBlank()) {
            throw new ExceptionBadRequest("Los campos no pueden ser nulos o vacíos");
        }
        try {
            SubjectTeachersEntity entity = convertToSubjectEntity(dto);
            SubjectTeachersEntity saved = repo.save(entity);
            return convertToSubjectTeachersDTO(saved);
        } catch (Exception e) {
            log.error("Error al registrar al profesor de la materia: " + e.getMessage());
            throw new ExceptionServerError("Error interno al registrar al maestro");
        }
    }

    public SubjectTeachersDTO updateSubjectTeacher(String id, SubjectTeachersDTO json) {
        SubjectTeachersEntity exists = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Maestro no encontrado"));

        if (json.getSubjectID() != null) {
            SubjectDefinitionsEntity subjectDefinitions = repoSubjectDefinitions.findById(json.getSubjectID())
                    .orElseThrow(() -> new ExceptionNotFound("Materia no encontrada con ID " + json.getSubjectID()));
            exists.setSubjectDefinitions(subjectDefinitions);
        } else {
            exists.setSubjectDefinitions(null);
        }

        if (json.getEmployeeID() != null) {
            EmployeeEntity employee = repoEmployee.findById(json.getEmployeeID())
                    .orElseThrow(() -> new ExceptionNotFound("Empleado no encontrado con ID " + json.getEmployeeID()));
            exists.setEmployee(employee);
        } else {
            exists.setEmployee(null);
        }

        try {
            SubjectTeachersEntity updateSubTeacher = repo.save(exists);
            return convertToSubjectTeachersDTO(updateSubTeacher);
        } catch (Exception e) {
            log.error("Error al actualizar el docente con ID " + id, e);
            throw new ExceptionServerError("Error interno al actualizar el docente");
        }
    }

    public boolean deleteSubjectTeacher(String id) {
        try {
            SubjectTeachersEntity exists = repo.findById(id).orElse(null);
            if (exists != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("No se encontró maestro con ID " + id + " para eliminar");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró maestro con ID " + id + " para eliminar");
        } catch (Exception e) {
            log.error("Error al intentar eliminar el docente con ID " + id, e);
            throw new ExceptionServerError("Error interno al intentar eliminar el docente");
        }
    }
}
