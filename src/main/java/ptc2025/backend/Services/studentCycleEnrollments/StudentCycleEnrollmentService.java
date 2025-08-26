package ptc2025.backend.Services.studentCycleEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;
import ptc2025.backend.Models.DTO.studentCycleEnrollments.StudentCycleEnrollmentDTO;
import ptc2025.backend.Respositories.StudentCareerEnrollments.StudentCareerEnrollmentsRepository;
import ptc2025.backend.Respositories.studentCycleEnrollments.StudentCycleEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCycleEnrollmentService {

    @Autowired
    private StudentCycleEnrollmentRepository repo;

    @Autowired
    private StudentCareerEnrollmentsRepository studentCareerEnrollmentsRepository;

    // GET
    public List<StudentCycleEnrollmentDTO> getEnrollments() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // POST
    public StudentCycleEnrollmentDTO insertEnrollment(StudentCycleEnrollmentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");

        StudentCycleEnrollmentEntity entity = convertToEntity(dto);
        StudentCycleEnrollmentEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // PUT
    public StudentCycleEnrollmentDTO updateEnrollment(String id, StudentCycleEnrollmentDTO dto) {
        return repo.findById(id).map(existing -> {
            existing.setStatus(dto.getStatus());
            existing.setRegisteredAt(dto.getRegisteredAt());
            existing.setCompletedAt(dto.getCompletedAt());

            StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));
            existing.setStudentCareerEnrollment(sce);

            return convertToDTO(repo.save(existing));
        }).orElseThrow(() -> new IllegalArgumentException("No se encontró inscripción con ID: " + id));
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
    private StudentCycleEnrollmentDTO convertToDTO(StudentCycleEnrollmentEntity entity) {
        return StudentCycleEnrollmentDTO.builder()
                .id(entity.getId())
                .studentCareerEnrollmentId(entity.getStudentCareerEnrollment().getStudentCareerEnrollmentID())
                .status(entity.getStatus())
                .registeredAt(entity.getRegisteredAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

    private StudentCycleEnrollmentEntity convertToEntity(StudentCycleEnrollmentDTO dto) {
        StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));

        return StudentCycleEnrollmentEntity.builder()
                .id(dto.getId())
                .studentCareerEnrollment(sce)
                .status(dto.getStatus())
                .registeredAt(dto.getRegisteredAt())
                .completedAt(dto.getCompletedAt())
                .build();
    }
}
