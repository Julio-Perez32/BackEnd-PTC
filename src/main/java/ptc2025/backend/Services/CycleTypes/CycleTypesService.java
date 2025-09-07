package ptc2025.backend.Services.CycleTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.CycleTypes.CycleTypesDTO;
import ptc2025.backend.Respositories.CycleTypes.CycleTypesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CycleTypesService {

    @Autowired
    private CycleTypesRepository repo;

    @Autowired
    private UniversityRespository repoUniversity;

    public List<CycleTypesDTO> getAllCycleTypes() {
        List<CycleTypesEntity> cycletype = repo.findAll();
        if (cycletype.isEmpty()) {
            throw new RuntimeException("No se encontraron registros de CycleTypes");
        }
        return cycletype.stream()
                .map(this::convertCycleTypeDTO)
                .collect(Collectors.toList());
    }

    public Page<CycleTypesDTO> getAllCycleTypesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CycleTypesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertCycleTypeDTO);
    }

    public CycleTypesDTO insertarCycleType(CycleTypesDTO data) {
        if (data == null || data.getCycleLabel() == null || data.getCycleLabel().isBlank()) {
            throw new IllegalArgumentException("Los datos no pueden estar vacíos o nulos");
        }

        try {
            CycleTypesEntity entity = convertirAEntity(data);
            CycleTypesEntity cycleTypeGuardado = repo.save(entity);
            return convertCycleTypeDTO(cycleTypeGuardado);
        } catch (Exception e) {
            log.error("Error al ingresar el tipo de ciclo: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al guardar el tipo de ciclo");
        }
    }

    public CycleTypesDTO convertCycleTypeDTO(CycleTypesEntity cycleType) {
        CycleTypesDTO dto = new CycleTypesDTO();
        dto.setId(cycleType.getId());
        dto.setCycleLabel(cycleType.getCycleLabel());

        if (cycleType.getUniversity() != null) {
            dto.setUniversityName(cycleType.getUniversity().getUniversityName());
            dto.setUniversityID(cycleType.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    private CycleTypesEntity convertirAEntity(CycleTypesDTO data) {
        CycleTypesEntity entity = new CycleTypesEntity();
        entity.setId(data.getId());
        entity.setCycleLabel(data.getCycleLabel());

        if (data.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Universidad no encontrada con ID: " + data.getUniversityID()
                    ));
            entity.setUniversity(university);
        } else {
            entity.setUniversity(null);
        }

        return entity;
    }

    public CycleTypesDTO actualizarCycleTypes(String id, CycleTypesDTO json) {
        CycleTypesEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("CycleType no encontrado con ID: " + id));

        if (json.getCycleLabel() == null || json.getCycleLabel().isBlank()) {
            throw new IllegalArgumentException("El campo 'cycleLabel' no puede estar vacío");
        }

        existente.setCycleLabel(json.getCycleLabel());

        if (json.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Universidad no encontrada con ID: " + json.getUniversityID()
                    ));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }

        try {
            CycleTypesEntity cycleTypeActualizado = repo.save(existente);
            return convertCycleTypeDTO(cycleTypeActualizado);
        } catch (Exception e) {
            log.error("Error al actualizar el tipo de ciclo: {}", e.getMessage(), e);
            throw new RuntimeException("Error interno al actualizar el tipo de ciclo");
        }
    }

    public boolean eliminarCycleType(String id) {
        try {
            CycleTypesEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró el CycleType con ID: " + id, 1);
        } catch (Exception e) {
            log.error("Error al eliminar el CycleType con ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error interno al eliminar el tipo de ciclo");
        }
    }
}
