package ptc2025.backend.Services.courseEnrollments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // GET DE PAGINACION
    public Page<CourseEnrollmentDTO> getPaginatedEnrollments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable).map(this::convertToDTO);
    }


    // POST
    public CourseEnrollmentDTO insertEnrollment(CourseEnrollmentDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Inscripción no puede ser nula");
        if (repo.existsById(dto.getId())) throw new IllegalArgumentException("La inscripción ya existe");

        CourseEnrollmentEntity entity = convertToEntity(dto);
        CourseEnrollmentEntity saved = repo.save(entity);
        return convertToDTO(saved);
    }

    // PUT
    public CourseEnrollmentDTO updateEnrollment(String id, CourseEnrollmentDTO dto) {
        CourseEnrollmentEntity exist = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Inscripción no encontrada"));

        exist.setEnrollmentStatus(dto.getEnrollmentStatus());
        exist.setFinalGrade(dto.getFinalGrade());
        exist.setEnrollmentDate(dto.getEnrollmentDate());
        exist.setMeritUnit(dto.getMeritUnit());

        if (dto.getCourseOfferingId() != null) {
            CourseOfferingsEntity co = courseOfferingsRepository.findById(dto.getCourseOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró CourseOffering con ID: " + dto.getCourseOfferingId()));
            exist.setCourseOfferings(co);
        } else {
            exist.setCourseOfferings(null);
        }

        if (dto.getStudentCareerEnrollmentId() != null) {
            StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));
            exist.setStudentCareerEnrollments(sce);
        } else {
            exist.setStudentCareerEnrollments(null);
        }

        CourseEnrollmentEntity actualizado = repo.save(exist);
        return convertToDTO(actualizado);
    }

    // DELETE
    public boolean deleteEnrollment(String id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("La inscripción con ID " + id + " no existe", 1);
        }
    }

    // CONVERSIONES
    private CourseEnrollmentDTO convertToDTO(CourseEnrollmentEntity entity) {
        CourseEnrollmentDTO dto = new CourseEnrollmentDTO();
        dto.setId(entity.getId());

        if (entity.getCourseOfferings() != null) {
            dto.setCourseOfferingId(entity.getCourseOfferings().getCourseOfferingID());

            // courseOfferingName desde SubjectDefinitions
            if (entity.getCourseOfferings().getSubjectDefinitions() != null) {
                dto.setCourseOfferingName(entity.getCourseOfferings().getSubjectDefinitions().getSubjectName());
            } else {
                dto.setCourseOfferingName(null);
            }
        } else {
            dto.setCourseOfferingId(null);
            dto.setCourseOfferingName(null);
        }

        if (entity.getStudentCareerEnrollments() != null) {
            dto.setStudentCareerEnrollmentId(entity.getStudentCareerEnrollments().getStudentCareerEnrollmentID());


            if (entity.getStudentCareerEnrollments().getStudent() != null) {
                dto.setStudentName(entity.getStudentCareerEnrollments().getStudent().getPeople().getFirstName()
                        + " " + entity.getStudentCareerEnrollments().getStudent().getPeople().getLastName());
            } else {
                dto.setStudentName(null);
            }

            // careerName desde CareerEntity
            if (entity.getStudentCareerEnrollments().getCareer() != null) {
                dto.setCareerName(entity.getStudentCareerEnrollments().getCareer().getNameCareer());
            } else {
                dto.setCareerName(null);
            }

        } else {
            dto.setStudentCareerEnrollmentId(null);
            dto.setStudentName(null);
            dto.setCareerName(null);
        }

        dto.setEnrollmentStatus(entity.getEnrollmentStatus());
        dto.setFinalGrade(entity.getFinalGrade());
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setMeritUnit(entity.getMeritUnit());

        return dto;
    }


    private CourseEnrollmentEntity convertToEntity(CourseEnrollmentDTO dto) {
        CourseEnrollmentEntity entity = new CourseEnrollmentEntity();
        entity.setId(dto.getId());
        entity.setEnrollmentStatus(dto.getEnrollmentStatus());
        entity.setFinalGrade(dto.getFinalGrade());
        entity.setEnrollmentDate(dto.getEnrollmentDate());
        entity.setMeritUnit(dto.getMeritUnit());

        if (dto.getCourseOfferingId() != null) {
            CourseOfferingsEntity co = courseOfferingsRepository.findById(dto.getCourseOfferingId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró CourseOffering con ID: " + dto.getCourseOfferingId()));
            entity.setCourseOfferings(co);
        }

        if (dto.getStudentCareerEnrollmentId() != null) {
            StudentCareerEnrollmentsEntity sce = studentCareerEnrollmentsRepository.findById(dto.getStudentCareerEnrollmentId())
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró StudentCareerEnrollment con ID: " + dto.getStudentCareerEnrollmentId()));
            entity.setStudentCareerEnrollments(sce);
        }

        return entity;
    }
}
