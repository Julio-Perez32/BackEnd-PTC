package ptc2025.backend.Services.studentCycleEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Entities.studentCycleEnrollments.StudentCycleEnrollmentEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.studentCycleEnrollments.StudentCycleEnrollmentDTO;
import ptc2025.backend.Respositories.StudentCareerEnrollments.StudentCareerEnrollmentsRepository;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;
import ptc2025.backend.Respositories.studentCycleEnrollments.StudentCycleEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentCycleEnrollmentService {

    @Autowired
    private StudentCycleEnrollmentRepository repo;

    @Autowired
    private StudentCareerEnrollmentsRepository studentCareerEnrollmentsRepo;

    @Autowired
    private YearCyclesRepository yearCyclesRepo;

    // GET
    public List<StudentCycleEnrollmentDTO> getEnrollments() {
        try {
            return repo.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ExceptionServerError("Error al obtener las inscripciones: " + e.getMessage());
        }
    }

    public Page<StudentCycleEnrollmentDTO> getStudentCycleEnrollmentPagination(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<StudentCycleEnrollmentEntity> pageEntity = repo.findAll(pageable);
            return pageEntity.map(this::convertToDTO);
        } catch (Exception e) {
            throw new ExceptionServerError("Error al paginar las inscripciones: " + e.getMessage());
        }
    }

    // POST
    public StudentCycleEnrollmentDTO insertEnrollment(StudentCycleEnrollmentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");
        try {
            StudentCycleEnrollmentEntity entity = convertToEntity(dto);
            StudentCycleEnrollmentEntity saved = repo.save(entity);
            return convertToDTO(saved);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionServerError("Error al insertar la inscripción: " + e.getMessage());
        }
    }

    // PUT
    public StudentCycleEnrollmentDTO updateEnrollment(String id, StudentCycleEnrollmentDTO dto) {
        try {
            return repo.findById(id).map(existing -> {
                existing.setStatus(dto.getStatus());
                existing.setRegisteredAt(dto.getRegisteredAt());
                existing.setCompletedAt(dto.getCompletedAt());

                StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepo.findById(dto.getStudentCareerEnrollmentId())
                        .orElseThrow(() -> new IllegalArgumentException("No se encontró la inscripción de la carrera del estudiante con ID: " + dto.getStudentCareerEnrollmentId()));
                existing.setStudentCareerEnrollment(sce);

                YearCyclesEntity yearCycles = yearCyclesRepo.findById(dto.getYearCycleID())
                        .orElseThrow(() -> new IllegalArgumentException("No se encontró YearCycle con ID: " + dto.getYearCycleID()));
                existing.setYearCycles(yearCycles);

                return convertToDTO(repo.save(existing));
            }).orElseThrow(() -> new ExceptionNotFound("No se encontró inscripción con ID: " + id));
        } catch (IllegalArgumentException | ExceptionNotFound e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionServerError("Error al actualizar la inscripción: " + e.getMessage());
        }
    }

    // DELETE
    public boolean deleteEnrollment(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("La inscripción con ID " + id + " no existe");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("La inscripción con ID " + id + " no existe");
        } catch (Exception e) {
            throw new ExceptionServerError("Error al eliminar la inscripción: " + e.getMessage());
        }
    }

    // CONVERSIONES
    private StudentCycleEnrollmentDTO convertToDTO(StudentCycleEnrollmentEntity entity) {
        StudentCycleEnrollmentDTO dto = new StudentCycleEnrollmentDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setRegisteredAt(entity.getRegisteredAt());
        dto.setCompletedAt(entity.getCompletedAt());

        if (entity.getStudentCareerEnrollment() != null) {
            dto.setStudentcareerenrollment(entity.getStudentCareerEnrollment().getStudentCareerEnrollmentID());
        } else {
            dto.setStudentcareerenrollment(null);
        }

        if (entity.getYearCycles() != null) {
            dto.setYearcycle(entity.getYearCycles().getId());
        } else {
            dto.setYearcycle(null);
        }
        return dto;
    }

    private StudentCycleEnrollmentEntity convertToEntity(StudentCycleEnrollmentDTO dto) {
        StudentCycleEnrollmentEntity entity = new StudentCycleEnrollmentEntity();
        entity.setStatus(dto.getStatus());
        entity.setRegisteredAt(dto.getRegisteredAt());
        entity.setCompletedAt(dto.getCompletedAt());

        if (dto.getStudentCareerEnrollmentId() != null) {
            StudentCareerEnrollmentsEntity studentCareerEnrollments = studentCareerEnrollmentsRepo.findById(dto.getStudentCareerEnrollmentId())
                    .orElseThrow(() -> new IllegalArgumentException("StudentCareerEnrollment no encontrada con ID: " + dto.getStudentCareerEnrollmentId()));
            entity.setStudentCareerEnrollment(studentCareerEnrollments);
        }

        if (dto.getYearCycleID() != null) {
            YearCyclesEntity yearCycles = yearCyclesRepo.findById(dto.getYearCycleID())
                    .orElseThrow(() -> new IllegalArgumentException("Ciclo de año no encontrado con ID: " + dto.getYearCycleID()));
            entity.setYearCycles(yearCycles);
        }
        return entity;
    }
}
