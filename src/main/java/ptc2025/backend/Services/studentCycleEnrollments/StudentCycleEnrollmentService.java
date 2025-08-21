package ptc2025.backend.Services.studentCycleEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;
import ptc2025.backend.Models.DTO.studentCycleEnrollments.StudentCycleEnrollmentDTO;
import ptc2025.backend.Respositories.studentCycleEnrollments.StudentCycleEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCycleEnrollmentService {

    @Autowired
    private StudentCycleEnrollmentRepository repo;

    // GET
    public List<StudentCycleEnrollmentDTO> getEnrollments() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // POST
    public StudentCycleEnrollmentDTO insertEnrollment(StudentCycleEnrollmentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");
        if (repo.existsById(dto.getId())) throw new IllegalArgumentException("Ya existe una inscripción con ese ID");
        StudentCycleEnrollmentEntity entity = convertirAEntity(dto);
        StudentCycleEnrollmentEntity saved = repo.save(entity);
        return convertirADTO(saved);
    }

    // PUT
    public StudentCycleEnrollmentDTO updateEnrollment(String id, StudentCycleEnrollmentDTO dto) {
        try {
            if (repo.existsById(id)) {
                StudentCycleEnrollmentEntity entity = repo.getById(id);
                entity.setStudentId(dto.getStudentId());
                entity.setCycleId(dto.getCycleId());
                entity.setEnrollmentDate(dto.getEnrollmentDate());
                entity.setStatus(dto.getStatus());
                entity.setIsActive(dto.getIsActive());
                StudentCycleEnrollmentEntity saved = repo.save(entity);
                return convertirADTO(saved);
            }
            throw new IllegalArgumentException("No se encontró inscripción con ID: " + id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la inscripción: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteEnrollment(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La inscripción con ID " + id + " no existe", 1);
        }
    }

    // CONVERSIONES
    private StudentCycleEnrollmentDTO convertirADTO(StudentCycleEnrollmentEntity entity) {
        StudentCycleEnrollmentDTO dto = new StudentCycleEnrollmentDTO();
        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudentId());
        dto.setCycleId(entity.getCycleId());
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setStatus(entity.getStatus());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private StudentCycleEnrollmentEntity convertirAEntity(StudentCycleEnrollmentDTO dto) {
        StudentCycleEnrollmentEntity entity = new StudentCycleEnrollmentEntity();
        entity.setId(dto.getId());
        entity.setStudentId(dto.getStudentId());
        entity.setCycleId(dto.getCycleId());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setStatus(dto.getStatus());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }
}
