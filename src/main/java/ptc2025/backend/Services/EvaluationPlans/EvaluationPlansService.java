package ptc2025.backend.Services.EvaluationPlans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CourseOfferings.CourseOfferingsEntity;
import ptc2025.backend.Entities.EvaluationPlans.EvaluationPlansEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.EvaluationPlans.EvaluationPlansDTO;
import ptc2025.backend.Respositories.CourseOfferings.CourseOfferingsRepository;
import ptc2025.backend.Respositories.EvaluationPlans.EvaluationPlansRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EvaluationPlansService {

    @Autowired
    private EvaluationPlansRepository repo;

    @Autowired
    private CourseOfferingsRepository courseOfferingsRepo;

    // GET
    public List<EvaluationPlansDTO> getEvaluationPlans() {
        List<EvaluationPlansEntity> plan = repo.findAll();
        if (plan.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen planes de evaluación registrados");
        }
        return plan.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<EvaluationPlansDTO> getEvaluationPlansPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EvaluationPlansEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen planes de evaluación para mostrar en la página solicitada");
        }
        return pageEntity.map(this::convertirADTO);
    }

    // POST
    public EvaluationPlansDTO insertEvaluationPlan(EvaluationPlansDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El plan de evaluación no puede ser nulo o vacío");
        }
        try {
            EvaluationPlansEntity entity = convertirAEntity(dto);
            EvaluationPlansEntity save = repo.save(entity);
            return convertirADTO(save);
        } catch (Exception e) {
            log.error("Error al insertar el plan de evaluación: {}", e.getMessage());
            throw new RuntimeException("No se puede crear el plan de evaluación: " + e.getMessage());
        }
    }

    // PUT
    public EvaluationPlansDTO updateEvaluationPlan(String id, EvaluationPlansDTO dto) {
        EvaluationPlansEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("No se encontró un plan de evaluación con el ID: " + id));

        try {
            entity.setPlanName(dto.getPlanName());
            entity.setDescription(dto.getDescription());
            entity.setCreatedAt(dto.getCreatedAt());

            if (dto.getCourseOfferingID() != null) {
                CourseOfferingsEntity courseOfferings = courseOfferingsRepo.findById(dto.getCourseOfferingID())
                        .orElseThrow(() -> new ExceptionNoSuchElement("Oferta de cursos no encontrada con ID: " + dto.getCourseOfferingID()));
                entity.setCourseOfferings(courseOfferings);
            } else {
                entity.setCourseOfferings(null);
            }

            EvaluationPlansEntity updated = repo.save(entity);
            return convertirADTO(updated);

        } catch (Exception e) {
            log.error("Error al actualizar el plan de evaluación con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo actualizar el plan de evaluación: " + e.getMessage());
        }
    }

    // DELETE

    public boolean deleteEvaluationPlan(String id) {
        try {
            if (!repo.existsById(id)) {
                throw new ExceptionNoSuchElement("No se encontró un plan de evaluación con el ID: " + id);
            }
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar. El plan de evaluación con ID " + id + " no existe.");
        }
    }

    // Convertir a DTO
    private EvaluationPlansDTO convertirADTO(EvaluationPlansEntity entity) {
        EvaluationPlansDTO dto = new EvaluationPlansDTO();
        dto.setEvaluationPlanID(entity.getEvaluationPlanID());
        dto.setPlanName(entity.getPlanName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getCourseOfferings() != null) {
            dto.setCourseOfferingID(entity.getCourseOfferings().getCourseOfferingID());
        } else {
            dto.setCourseoffering("Sin oferta de cursos Asignada");
            dto.setCourseOfferingID(null);
        }
        return dto;
    }

    private EvaluationPlansEntity convertirAEntity(EvaluationPlansDTO dto) {
        EvaluationPlansEntity entity = new EvaluationPlansEntity();
        // NO setees evaluationPlanID (lo genera la DB)
        entity.setPlanName(dto.getPlanName());
        entity.setDescription(dto.getDescription());

        // respaldo por si alguien manda null: el @PrePersist igual lo cubrirá
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDate.now());
        }

        if (dto.getCourseOfferingID() != null) {
            CourseOfferingsEntity offering = courseOfferingsRepo.findById(dto.getCourseOfferingID())
                    .orElseThrow(() -> new ExceptionNoSuchElement(
                            "Oferta de cursos no encontrada con ID: " + dto.getCourseOfferingID()));
            entity.setCourseOfferings(offering);
        } else {
            throw new IllegalArgumentException("courseOfferingID es requerido");
        }

        return entity;
    }
}

/**EVALUATIONPLANID
 COURSEOFFERINGID
 PLANNAME
 DESCRIPTION
 CREATEDAT*/

/**evaluationPlanID
 courseOfferingID
 planName
 description
 createdAt*/
//EvaluationPlans