package ptc2025.backend.Services.EntityType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EntityType.EntityTypesEntity;
import ptc2025.backend.Entities.PlanComponents.PlanComponentsEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Models.DTO.EntityType.EntityTypesDTO;
import ptc2025.backend.Models.DTO.PlanComponents.PlanComponentsDTO;
import ptc2025.backend.Respositories.EntityType.EntityTypesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EntityTypesService {

    @Autowired
    EntityTypesRepository repo;

    @Autowired // Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    // Get
    public List<EntityTypesDTO> getEntityTypes() {
        List<EntityTypesEntity> components = repo.findAll();

        if (components.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron tipos de entidad.");
        }

        return components.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<EntityTypesDTO> getEntityTypesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EntityTypesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron tipos de entidad en la página solicitada.");
        }
        return pageEntity.map(this::convertirADTO);
    }

    public EntityTypesDTO insertEntityType(EntityTypesDTO dto) {
        // Validaciones
        if (dto == null ||
                dto.getEntityType() == null || dto.getEntityType().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios, no dejarlos vacíos");
        }

        if (dto.getEntityType().length() > 60) {
            throw new IllegalArgumentException("El tipo de entidad no debe tener más de 60 caracteres");
        }

        try {
            EntityTypesEntity entity = convertirAEntity(dto);
            EntityTypesEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al guardar el tipo de entidad: " + e.getMessage());
            throw new RuntimeException("Error interno al guardar el tipo de entidad");
        }
    }

    public EntityTypesDTO updateEntityType(String id, EntityTypesDTO dto) {
        try {
            EntityTypesEntity existente = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNoSuchElement("El tipo de entidad con ID " + id + " no existe."));

            existente.setEntityType(dto.getEntityType());

            if (dto.getUniversityID() != null) {
                UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                        .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
                existente.setUniversity(university);
            } else {
                existente.setUniversity(null);
            }

            EntityTypesEntity actualizado = repo.save(existente);
            return convertirADTO(actualizado);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("El tipo de entidad no pudo ser editado: " + e.getMessage());
        }
    }

    public boolean deleteEntityType(String id) {
        try {
            EntityTypesEntity objCompo = repo.findById(id)
                    .orElseThrow(() -> new ExceptionNoSuchElement("Tipo de entidad no encontrado con el ID: " + id));

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException(
                    "No se encontró ningún tipo de entidad con el ID: " + id + " para poder ser eliminado", 1);
        }
    }

    private EntityTypesDTO convertirADTO(EntityTypesEntity entity) {
        EntityTypesDTO dto = new EntityTypesDTO();
        dto.setEntityTypeID(entity.getEntityTypeID());
        dto.setEntityType(entity.getEntityType());

        if (entity.getUniversity() != null) {
            dto.setUniversityName(entity.getUniversity().getUniversityName());
            dto.setUniversityID(entity.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setEntityTypeID(null);
        }
        return dto;
    }

    public EntityTypesEntity convertirAEntity(EntityTypesDTO dto) {
        EntityTypesEntity entity = new EntityTypesEntity();
        entity.setEntityType(dto.getEntityType());

        if (dto.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(dto.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }
}
