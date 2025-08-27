package ptc2025.backend.Services.StudentEvaluations;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.StudentEvaluations.StudentEvaluationsEntity;
import ptc2025.backend.Entities.Students.StudentsEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;
import ptc2025.backend.Models.DTO.StudentEvaluations.StudentEvaluationsDTO;
import ptc2025.backend.Respositories.PlanComponents.PlanComponentsRespository;
import ptc2025.backend.Respositories.StudentEvaluations.StudentEvaluationsRepository;
import ptc2025.backend.Respositories.courseEnrollments.CourseEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@CrossOrigin
public class StudentEvaluationsService {

    @Autowired
    StudentEvaluationsRepository repo;

    @Autowired
    PlanComponentsRespository planComponentsRespo;

    @Autowired
    CourseEnrollmentRepository courseEnrollmentRepo;

    public List<StudentEvaluationsDTO> getAllStudentsEvaluations() {
        List<StudentEvaluationsEntity> students = repo.findAll();
        return students.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

    }

    private StudentEvaluationsDTO convertirADTO(StudentEvaluationsEntity entity) {
        StudentEvaluationsDTO dto = new StudentEvaluationsDTO();
        dto.setId(entity.getId());
        dto.setScore(entity.getScore());
        dto.setFeedback(entity.getFeedback());
        dto.setSubmittedAt(entity.getSubmittedAt());

        if(entity.getPlanComponents() != null){
            dto.setComponent(entity.getPlanComponents().getComponentID());
        }else {
            dto.setComponent("Sin componentes Asignado");
            dto.setComponent(null);
        }
        if(entity.getCourseEnrollment() != null){
            dto.setCourseEnrollment(entity.getCourseEnrollment().getId());
        }else {
            dto.setCourseEnrollment("Sin CourseEnrollment Asignado");
            dto.setCourseEnrollment(null);
        }
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
        entity.setScore(data.getScore());
        entity.setFeedback(data.getFeedback());
        entity.setSubmittedAt(data.getSubmittedAt());
        if(data.getCourseEnrollmentID() != null){
            CourseEnrollmentEntity courseEnrollment = courseEnrollmentRepo.findById(data.getCourseEnrollmentID())
                    .orElseThrow(() -> new IllegalArgumentException("CourseEnrollments no encontrado con ID: " + data.getCourseEnrollmentID()));
            entity.setCourseEnrollment(courseEnrollment);
        }
        if(data.getComponentID() != null){
            PlanComponentsEntity planComponents = planComponentsRespo.findById(data.getComponentID())
                    .orElseThrow(() -> new IllegalArgumentException("EvaluationPlanComponent no encontrado con ID: " + data.getComponentID()));
            entity.setPlanComponents(planComponents);
        }
        return entity;
    }

    public StudentEvaluationsDTO ActualizarRegistro(String id, StudentEvaluationsDTO json) {
        StudentEvaluationsEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setScore(json.getScore());
        existente.setFeedback(json.getFeedback());
        existente.setSubmittedAt(json.getSubmittedAt());

        if(json.getComponentID() != null){
            PlanComponentsEntity planComponents = planComponentsRespo.findById(json.getComponentID())
                    .orElseThrow(() -> new IllegalArgumentException("EvaluationPlanComponent no encontrado con ID: " + json.getComponentID()));
            existente.setPlanComponents(planComponents);
        }else {
            existente.setPlanComponents(null);
        }
        if(json.getCourseEnrollmentID() != null){
            CourseEnrollmentEntity courseEnrollment = courseEnrollmentRepo.findById(json.getCourseEnrollmentID())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de año no encontrado con ID: " + json.getCourseEnrollmentID()));
            existente.setCourseEnrollment(courseEnrollment);
        }else {
            existente.setCourseEnrollment(null);
        }
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
