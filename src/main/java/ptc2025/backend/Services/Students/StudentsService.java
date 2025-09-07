package ptc2025.backend.Services.Students;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Models.DTO.Students.StudentsDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.Students.StudentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentsService {

    @Autowired
    StudentsRepository repo;

    @Autowired
    private PeopleRepository repoPeople;

    public List<StudentsDTO> getAllStudents() {
        List<StudentsEntity> students = repo.findAll();
        if (students.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron registros de estudiantes.");
        }
        return students.stream()
                .map(this::convertToStudentsDTO)
                .collect(Collectors.toList());
    }

    public Page<StudentsDTO> getStudentsPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentsEntity> pageEntity = repo.findAll(pageable);
        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron estudiantes en la paginación.");
        }
        return pageEntity.map(this::convertToStudentsDTO);
    }

    public StudentsDTO convertToStudentsDTO(StudentsEntity students) {
        StudentsDTO dto = new StudentsDTO();
        dto.setStudentID(students.getStudentID());
        dto.setStudentCode(students.getStudentCode());

        if (students.getPeople() != null) {
            dto.setPersonName(students.getPeople().getFirstName());
            dto.setPersonLastName(students.getPeople().getLastName());
            dto.setPersonID(students.getPeople().getPersonID());
        } else {
            dto.setPersonName("Sin Persona Asignada");
            dto.setPersonID(null);
        }
        return dto;
    }

    public StudentsEntity convertToStudentsEntity(StudentsDTO dto) {
        StudentsEntity entity = new StudentsEntity();
        entity.setStudentID(dto.getStudentID());
        entity.setStudentCode(dto.getStudentCode());

        if (dto.getPersonID() != null) {
            PeopleEntity people = repoPeople.findById(dto.getPersonID())
                    .orElseThrow(() -> new ExceptionNotFound("Persona no encontrada con ID: " + dto.getPersonID()));
            entity.setPeople(people);
        }
        return entity;
    }

    public StudentsDTO insertStudent(StudentsDTO dto) {
        if (dto == null || dto.getStudentID() == null || dto.getStudentID().isEmpty()
                || dto.getStudentCode() == null || dto.getStudentCode().isEmpty()) {
            throw new IllegalArgumentException("Los campos del estudiante deben estar completos.");
        }
        try {
            StudentsEntity entity = convertToStudentsEntity(dto);
            StudentsEntity studentSaved = repo.save(entity);
            return convertToStudentsDTO(studentSaved);
        } catch (Exception e) {
            log.error("Error al registrar el estudiante: " + e.getMessage());
            throw new RuntimeException("Error interno al registrar el estudiante.");
        }
    }

    public StudentsDTO updateStudent(String id, StudentsDTO json) {
        StudentsEntity existsStudent = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Estudiante no encontrado con ID: " + id));

        existsStudent.setStudentCode(json.getStudentCode());

        if (json.getPersonID() != null) {
            PeopleEntity person = repoPeople.findById(json.getPersonID())
                    .orElseThrow(() -> new ExceptionNotFound("Persona no encontrada con ID proporcionado: " + json.getPersonID()));
            existsStudent.setPeople(person);
        } else {
            existsStudent.setPeople(null);
        }

        StudentsEntity updatedStudent = repo.save(existsStudent);
        return convertToStudentsDTO(updatedStudent);
    }

    public boolean deleteStudent(String id) {
        try {
            StudentsEntity existsStudent = repo.findById(id).orElse(null);
            if (existsStudent != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró estudiante con ID: " + id + " para eliminar", 1);
        }
    }
}
