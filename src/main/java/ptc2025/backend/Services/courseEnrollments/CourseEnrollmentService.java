package ptc2025.backend.Services.courseEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;
import ptc2025.backend.Models.DTO.courseEnrollments.CourseEnrollmentDTO;
import ptc2025.backend.Respositories.courseEnrollments.CourseEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseEnrollmentService {

    @Autowired
    private CourseEnrollmentRepository repo;

    // Get
    public List<CourseEnrollmentDTO> getEnrollments() {
        List<CourseEnrollmentEntity> list = repo.findAll();
        return list.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Post
    public CourseEnrollmentDTO insertEnrollment(CourseEnrollmentDTO dto) {
        if(dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");
        try {
            CourseEnrollmentEntity entity = convertToEntity(dto);
            return convertToDTO(repo.save(entity));
        } catch(Exception e) {
            throw new RuntimeException("No se pudo crear la inscripción: " + e.getMessage());
        }
    }

    // Put
    public CourseEnrollmentDTO updateEnrollment(String id, CourseEnrollmentDTO dto) {
        try {
            if(repo.existsById(id)) {
                CourseEnrollmentEntity entity = repo.getById(id);
                entity.setStudentId(dto.getStudentId());
                entity.setOfferingId(dto.getOfferingId());
                entity.setEnrollmentDate(dto.getEnrollmentDate());
                entity.setGrade(dto.getGrade());
                entity.setIsActive(dto.getIsActive());
                return convertToDTO(repo.save(entity));
            }
            throw new IllegalArgumentException("La inscripción con ID " + id + " no pudo ser actualizada");
        } catch(Exception e) {
            throw new RuntimeException("No se pudo actualizar la inscripción: " + e.getMessage());
        }
    }

    // Delete
    public boolean deleteEnrollment(String id) {
        try {
            if(repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else return false;
        } catch(EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La inscripción con ID " + id + " no existe", 1);
        }
    }

    // Convert to DTO
    private CourseEnrollmentDTO convertToDTO(CourseEnrollmentEntity entity) {
        CourseEnrollmentDTO dto = new CourseEnrollmentDTO();
        dto.setEnrollmentId(entity.getEnrollmentId());
        dto.setStudentId(entity.getStudentId());
        dto.setOfferingId(entity.getOfferingId());
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setGrade(entity.getGrade());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private CourseEnrollmentEntity convertToEntity(CourseEnrollmentDTO dto) {
        CourseEnrollmentEntity entity = new CourseEnrollmentEntity();
        entity.setEnrollmentId(dto.getEnrollmentId());
        entity.setStudentId(dto.getStudentId());
        entity.setOfferingId(dto.getOfferingId());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setGrade(dto.getGrade());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }

}
