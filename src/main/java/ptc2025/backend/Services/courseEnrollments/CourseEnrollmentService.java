package ptc2025.backend.Services.courseEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.StudentCareerEnrollments.StudentCareerEnrollmentsEntity;
import ptc2025.backend.Entities.courseEnrollments.CourseEnrollmentEntity;
import ptc2025.backend.Models.DTO.courseEnrollments.CourseEnrollmentDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.StudentCareerEnrollments.StudentCareerEnrollmentsRepository;
import ptc2025.backend.Respositories.courseEnrollments.CourseEnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseEnrollmentService {

    @Autowired
    private CourseEnrollmentRepository repo;

    @Autowired
    private CourseOfferingsRepository courseOfferingsRepository;

    @Autowired
    private StudentCareerEnrollmentsRepository studentCareerEnrollmentsRepository;

    // GET
    public List<CourseEnrollmentDTO> getEnrollments() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // POST
    public CourseEnrollmentDTO insertEnrollment(CourseEnrollmentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");
        CourseEnrollmentEntity entity = convertToEntity(dto);
        return convertToDTO(repo.save(entity));
    }

    // PUT
    public CourseEnrollmentDTO updateEnrollment(String id, CourseEnrollmentDTO dto) {
        return repo.findById(id).map(existing -> {
            existing.setEnrollmentStatus(dto.getEnrollmentStatus());
            existing.setFinalGrade(dto.getFinalGrade());
            existing.setEnrollmentDate(dto.getEnrollmentDate());
            existing.setMeritUnit(dto.getMeritUnit());

            CourseOfferingsEntity co = courseOfferingsRepository.findById(dto.getCourseOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró CourseOffering con ID: " + dto.getCourseOfferingId()));
            existing.setCourseOfferings(co);

            StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));
            existing.setStudentCareerEnrollments(sce);

            return convertToDTO(repo.save(existing));
        }).orElseThrow(() -> new IllegalArgumentException("No se encontró inscripción con ID: " + id));
    }

    // DELETE
    public boolean deleteEnrollment(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La inscripción con ID " + id + " no existe", 1);
        }
    }

    // CONVERSIONES
    private CourseEnrollmentDTO convertToDTO(CourseEnrollmentEntity entity) {
        return CourseEnrollmentDTO.builder()
                .id(entity.getId())
                .courseOfferingId(entity.getCourseOfferings().getCourseOfferingID())
                .studentCareerEnrollmentId(entity.getStudentCareerEnrollments().getStudentCareerEnrollmentID())
                .enrollmentStatus(entity.getEnrollmentStatus())
                .finalGrade(entity.getFinalGrade())
                .enrollmentDate(entity.getEnrollmentDate())
                .meritUnit(entity.getMeritUnit())
                .build();
    }

    private CourseEnrollmentEntity convertToEntity(CourseEnrollmentDTO dto) {
        CourseOfferingsEntity co = courseOfferingsRepository.findById(dto.getCourseOfferingId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró CourseOffering con ID: " + dto.getCourseOfferingId()));

        StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));

        return CourseEnrollmentEntity.builder()
                .id(dto.getId())
                .courseOfferings(co)
                .studentCareerEnrollments(sce)
                .enrollmentStatus(dto.getEnrollmentStatus())
                .finalGrade(dto.getFinalGrade())
                .enrollmentDate(dto.getEnrollmentDate())
                .meritUnit(dto.getMeritUnit())
                .build();
    }
}
