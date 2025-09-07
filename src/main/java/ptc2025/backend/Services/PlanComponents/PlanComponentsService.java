package ptc2025.backend.Services.PlanComponents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ptc2025.backend.Entities.EvaluationPlans.EvaluationPlansEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.PlanComponents.PlanComponentsDTO;
import ptc2025.backend.Respositories.EvaluationPlans.EvaluationPlansRepository;
import ptc2025.backend.Respositories.PlanComponents.PlanComponentsRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlanComponentsService {
    @Autowired
    PlanComponentsRespository repo;

    @Autowired
    EvaluationPlansRepository evaluationPlansRepo;

    //Get
    public List<PlanComponentsDTO> getPlanComponent() {
        List<PlanComponentsEntity> components = repo.findAll();
        return components.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<PlanComponentsDTO> getPlanComponentPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlanComponentsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    //POST
    public PlanComponentsDTO insertPlanComponents(@RequestBody PlanComponentsDTO dto) {
        if (dto == null ||
                dto.getEvaluationPlanID() == null || dto.getEvaluationPlanID().isBlank() ||
                dto.getComponentName() == null || dto.getComponentName().isBlank() ||
                dto.getWeightPercentage() == null ||
                dto.getWeightPercentage() <= 0 || dto.getWeightPercentage() > 100 ||
                (dto.getOrderIndex() != null && dto.getOrderIndex() < 1)) {
            throw new ExceptionBadRequest("Todos los campos obligatorios deben estar completos y válidos");
        }

        try {
            PlanComponentsEntity entity = convertirAEntity(dto);
            PlanComponentsEntity guardardo = repo.save(entity);
            return convertirADTO(guardardo);
        } catch (Exception e) {
            log.error("Error al guardar el componente de evaluación: " + e.getMessage());
            throw new RuntimeException("Error interno al registrar el componente");
        }
    }

    //PUT
    public PlanComponentsDTO updatePlanComponents(String id, PlanComponentsDTO dto) {
        PlanComponentsEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("El componente de evaluación con ID: " + id + " no existe"));

        existente.setRubric(dto.getRubric());
        existente.setComponentName(dto.getComponentName());
        existente.setWeightPercentage(dto.getWeightPercentage());
        existente.setOrderIndex(dto.getOrderIndex());

        if (dto.getEvaluationPlanID() != null) {
            EvaluationPlansEntity evaluationPlans = evaluationPlansRepo.findById(dto.getEvaluationPlanID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Plan de evaluación no encontrado con ID: " + dto.getEvaluationPlanID()));
            existente.setEvaluationPlans(evaluationPlans);
        } else {
            existente.setEvaluationPlans(null);
        }

        PlanComponentsEntity actualizar = repo.save(existente);
        return convertirADTO(actualizar);
    }

    //DELETE
    public boolean deletePlanComponents(String id) {
        try {
            PlanComponentsEntity objCompo = repo.findById(id).orElse(null);
            if (objCompo != null) {
                repo.deleteById(id);
                return true;
            } else {
                System.out.println("Componente de evaluación no encontrado");
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("No se encontró ningún componente de evaluación con el ID: " + id + " para poder ser eliminado");
        }
    }

    //Convertir a DTO
    private PlanComponentsDTO convertirADTO(PlanComponentsEntity entity) {
        PlanComponentsDTO dto = new PlanComponentsDTO();
        dto.setComponentID(entity.getComponentID());
        dto.setRubric(entity.getRubric());
        dto.setComponentName(entity.getComponentName());
        dto.setWeightPercentage(entity.getWeightPercentage());
        dto.setOrderIndex(entity.getOrderIndex() != null ? entity.getOrderIndex() : 1);

        if (entity.getEvaluationPlans() != null) {
            dto.setEvaluationplans(entity.getEvaluationPlans().getPlanName());
            dto.setEvaluationPlanID(entity.getEvaluationPlans().getEvaluationPlanID());
        } else {
            dto.setEvaluationplans("Sin plan de evaluación asignada");
            dto.setEvaluationPlanID(null);
        }
        return dto;
    }

    //Convertir a Entity
    public PlanComponentsEntity convertirAEntity(PlanComponentsDTO dto) {
        PlanComponentsEntity entity = new PlanComponentsEntity();
        entity.setRubric(dto.getRubric());
        entity.setComponentName(dto.getComponentName().trim());
        entity.setWeightPercentage(dto.getWeightPercentage());

        // Si no se envía orderIndex, se usa 1 por defecto
        entity.setOrderIndex(dto.getOrderIndex() != null ? dto.getOrderIndex() : 1);

        if (dto.getEvaluationPlanID() != null) {
            EvaluationPlansEntity evaluationPlans = evaluationPlansRepo.findById(dto.getEvaluationPlanID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Plan de evaluación no encontrado con ID: " + dto.getEvaluationPlanID()));
            entity.setEvaluationPlans(evaluationPlans);
        }

        return entity;
    }
}
