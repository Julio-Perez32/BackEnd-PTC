package ptc2025.backend.Services.Students;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Models.DTO.Students.StudentsDTO;
import ptc2025.backend.Respositories.Students.StudentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentsService {

    @Autowired
    StudentsRepository repo;

    public List<StudentsDTO> getAllStudents() {
        List<StudentsEntity> students = repo.findAll();
        return students.stream()
                .map(this::convertToStudentsDTO)
                .collect(Collectors.toList());
    }

    public StudentsDTO convertToStudentsDTO(StudentsEntity students) {
        StudentsDTO dto = new StudentsDTO();
        dto.setStudentID(students.getStudentID());
        dto.setPersonID(students.getPersonID());
        dto.setStudentCode(students.getStudentCode());
        return dto;
    }

    public StudentsEntity convertToStudentsEntity(StudentsDTO dto) {
        StudentsEntity entity = new StudentsEntity();
        entity.setStudentID(dto.getStudentID());
        entity.setPersonID(dto.getPersonID());
        entity.setStudentCode(dto.getStudentCode());
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
            throw new IllegalArgumentException("Error al registrar el estudiante");
        }
    }

    public StudentsDTO updateStudent(String id, StudentsDTO json){
        StudentsEntity existsStudent = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado."));
        existsStudent.setPersonID(json.getPersonID());
        existsStudent.setStudentCode(json.getStudentCode());

        StudentsEntity updatedStudent = repo.save(existsStudent);

        return convertToStudentsDTO(updatedStudent);
    }

    public boolean deleteStudent(String id){
        try{
            StudentsEntity existsStudent = repo.findById(id).orElse(null);
            if(existsStudent != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        } catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró estudiante con ID: " + id + " para eliminar",1);
        }
    }
}
