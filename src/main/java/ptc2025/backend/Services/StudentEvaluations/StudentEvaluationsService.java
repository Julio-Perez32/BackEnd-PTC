package ptc2025.backend.Services.StudentEvaluations;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Models.DTO.StudentEvaluations.StudentEvaluationsDTO;
import ptc2025.backend.Respositories.StudentEvaluations.StudentEvaluationsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentEvaluationsService {

    @Autowired
    StudentEvaluationsRepository repo;

    public List<StudentEvaluationsDTO> getAllStudentsEvaluations() {
        List<StudentEvaluationsEntity> students = repo.findAll();
        return students.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

    }

    private StudentEvaluationsDTO convertirADTO(StudentEvaluationsEntity entity) {
        StudentEvaluationsDTO dto = new StudentEvaluationsDTO();
        dto.setId(entity.getId());
        dto.setComponentID(entity.getComponentID());
        dto.setCourseEnrollmentID(entity.getCourseEnrollmentID());
        dto.setScore(entity.getScore());
        dto.setFeedback(entity.getFeedback());
        dto.setSubmittedAt(entity.getSubmittedAt());
        return dto;
    }

    public StudentEvaluationsDTO insertarDatos(StudentEvaluationsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no correctoss");
        }
        try {
            StudentEvaluationsEntity entity = convertirAEntity(data);
            StudentEvaluationsEntity registroGuardado = repo.save(entity);
            return convertirADTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al querer guardar los datos ingresados" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }

    }

    private StudentEvaluationsEntity convertirAEntity(StudentEvaluationsDTO data) {
        StudentEvaluationsEntity entity = new StudentEvaluationsEntity();
        entity.setComponentID(data.getComponentID());
        entity.setCourseEnrollmentID(data.getCourseEnrollmentID());
        entity.setScore(data.getScore());
        entity.setFeedback(data.getFeedback());
        entity.setSubmittedAt(data.getSubmittedAt());
        return entity;
    }

    public StudentEvaluationsDTO ActualizarRegistro(String id, StudentEvaluationsDTO json) {
        StudentEvaluationsEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setComponentID(json.getComponentID());
        existente.setCourseEnrollmentID(json.getCourseEnrollmentID());
        existente.setScore(json.getScore());
        existente.setFeedback(json.getFeedback());
        existente.setSubmittedAt(json.getSubmittedAt());
        StudentEvaluationsEntity RegistroActualizado = repo.save(existente);
        return convertirADTO(RegistroActualizado);

    }

    public boolean removerRegistro(String id) {
        try{
            StudentEvaluationsEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el  registro", 1);
        }
    }
}
