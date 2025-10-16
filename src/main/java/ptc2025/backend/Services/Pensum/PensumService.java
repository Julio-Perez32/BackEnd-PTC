package ptc2025.backend.Services.Pensum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Pensum.PensumEntity;
import ptc2025.backend.Entities.careers.CareerEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Pensum.PensumDTO;
import ptc2025.backend.Respositories.Pensum.PensumRepository;
import ptc2025.backend.Respositories.careers.CareerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PensumService {

    @Autowired
    PensumRepository repo;

    @Autowired
    CareerRepository repoCareer;

    public List<PensumDTO> getPensa() {
        List<PensumEntity> pensa = repo.findAll();
        return pensa.stream()
                .map(this::convertToPensumDTO)
                .collect(Collectors.toList());
    }

    public Page<PensumDTO> getPensumPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PensumEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertToPensumDTO);
    }

    // ✅ CORRECCIÓN: Incluir el nombre de la carrera
    private PensumDTO convertToPensumDTO(PensumEntity entity) {
        PensumDTO dto = new PensumDTO();
        dto.setPensumID(entity.getPensumID());
        dto.setVersion(entity.getVersion());
        dto.setEffectiveYear(entity.getEffectiveYear());

        if (entity.getCareer() != null) {
            dto.setCareerID(entity.getCareer().getId());
            // ✅ Agregar el nombre de la carrera para mostrar en el frontend
            dto.setCareer(entity.getCareer().getNameCareer());
        } else {
            dto.setCareerID(null);
            dto.setCareer(null);
        }

        return dto;
    }

    private PensumEntity convertToPensumEntity(PensumDTO dto) {
        PensumEntity entity = new PensumEntity();
        entity.setPensumID(dto.getPensumID());
        entity.setVersion(dto.getVersion());
        entity.setEffectiveYear(dto.getEffectiveYear());

        if (dto.getCareerID() != null) {
            CareerEntity career = repoCareer.findById(dto.getCareerID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Carrera no encontrada con ID " + dto.getCareerID()));
            entity.setCareer(career);
        } else {
            entity.setCareer(null);
        }
        return entity;
    }

    public PensumDTO insertPensum(PensumDTO dto) {
        // ✅ Validación mejorada
        if (dto == null) {
            throw new ExceptionBadRequest("El DTO no puede ser nulo");
        }
        if (dto.getCareerID() == null || dto.getCareerID().isEmpty()) {
            throw new ExceptionBadRequest("El ID de la carrera es obligatorio");
        }
        if (dto.getVersion() == null || dto.getVersion().isEmpty()) {
            throw new ExceptionBadRequest("La versión es obligatoria");
        }
        if (dto.getEffectiveYear() == null) {
            throw new ExceptionBadRequest("El año efectivo es obligatorio");
        }

        try {
            PensumEntity entity = convertToPensumEntity(dto);
            PensumEntity savedPensum = repo.save(entity);
            return convertToPensumDTO(savedPensum);
        } catch (ExceptionNoSuchElement e) {
            // Re-lanzar excepciones de validación
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar el pensum: " + e.getMessage(), e);
            throw new RuntimeException("Error al registrar el pensum: " + e.getMessage());
        }
    }

    public PensumDTO updatePensum(String Id, PensumDTO dto) {
        PensumEntity existsPensum = repo.findById(Id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Pensum no encontrado con ID: " + Id));

        existsPensum.setVersion(dto.getVersion());
        existsPensum.setEffectiveYear(dto.getEffectiveYear());

        if (dto.getCareerID() != null) {
            CareerEntity career = repoCareer.findById(dto.getCareerID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Carrera no encontrada con ID " + dto.getCareerID()));
            existsPensum.setCareer(career);
        } else {
            existsPensum.setCareer(null);
        }

        PensumEntity updatedPensum = repo.save(existsPensum);
        return convertToPensumDTO(updatedPensum);
    }

    public boolean deletePensum(String Id) {
        try {
            PensumEntity existsPensum = repo.findById(Id).orElse(null);
            if (existsPensum != null) {
                repo.deleteById(Id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("No se encontró pensum con ID: " + Id + " para eliminar");
        }
    }
}