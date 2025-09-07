package ptc2025.backend.Services.Requirements;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Requirements.RequirementsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Requirements.RequirementsDTO;
import ptc2025.backend.Respositories.Requirements.RequirementsRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class RequirementsService {

    @Autowired
    private RequirementsRepository repo;
    @Autowired
    private UniversityRespository repoUniversity;

    public List<RequirementsDTO> getAllRequirements(){
        List<RequirementsEntity> requirement = repo.findAll();
        return requirement.stream()
                .map(this::convertirRequerimientosADTO)
                .collect(Collectors.toList());
    }

    public Page<RequirementsDTO> getRequirementPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RequirementsEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirRequerimientosADTO);
    }

    public RequirementsDTO convertirRequerimientosADTO(RequirementsEntity reque){
        RequirementsDTO dto = new RequirementsDTO();
        dto.setId(reque.getId());
        dto.setRequirementName(reque.getRequirementName());
        dto.setDescription(reque.getDescription());
        if(reque.getUniversity() != null){
            dto.setUniversityName(reque.getUniversity().getUniversityName());
            dto.setUniversityID(reque.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public RequirementsDTO insertarDatos(RequirementsDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        try {
            RequirementsEntity entity = converirAEntity(data);
            RequirementsEntity RegistroGuardado = repo.save(entity);
            return convertirRequerimientosADTO(RegistroGuardado);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al ingresar los datos: {}", e.getMessage());
            throw new RuntimeException("Error al registrar el nuevo requerimiento", e);
        }
    }

    private RequirementsEntity converirAEntity(RequirementsDTO data) {
        RequirementsEntity entity = new RequirementsEntity();
        entity.setRequirementName(data.getRequirementName());
        entity.setDescription(data.getDescription());
        if(data.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }

    public RequirementsDTO actualizarRequirements(String id, RequirementsDTO json) {
        RequirementsEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Requirement no encontrado con ID: " + id));
        existente.setRequirementName(json.getRequirementName());
        existente.setDescription(json.getDescription());

        if(json.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }

        try {
            RequirementsEntity RequirementActualizado = repo.save(existente);
            return convertirRequerimientosADTO(RequirementActualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el requerimiento: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar el Requirement", e);
        }
    }

    public boolean eliminarRequirement(String id) {
        try {
            RequirementsEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNoSuchElement("Requirement no encontrado con ID: " + id));
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.error("Error al eliminar requerimiento: {}", e.getMessage());
            throw new EmptyResultDataAccessException("No se encontró el registro para eliminar", 1);
        }
    }
}
