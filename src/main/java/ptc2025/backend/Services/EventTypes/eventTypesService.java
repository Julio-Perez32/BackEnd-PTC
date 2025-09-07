package ptc2025.backend.Services.EventTypes;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EventTypes.eventTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.EventTypes.eventTypesDTO;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Respositories.EventTypes.eventTypesRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class eventTypesService {

    @Autowired
    private eventTypesRespository repo;

    public List<eventTypesDTO> getEventTypes() {
        List<eventTypesEntity> eventos = repo.findAll();
        if (eventos.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen tipos de eventos registrados.");
        }
        return eventos.stream()
                .map(this::covertirAeventTypesDTO)
                .collect(Collectors.toList());
    }

    public Page<eventTypesDTO> getEventTypesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<eventTypesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen tipos de eventos para mostrar en la página solicitada.");
        }

        return pageEntity.map(this::covertirAeventTypesDTO);
    }

    public eventTypesDTO insertarEvento(eventTypesDTO dto) {
        if (dto.getUniversityID() == null || dto.getUniversityID().isBlank() ||
                dto.getTypeName() == null || dto.getTypeName().isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        try {
            eventTypesEntity entidad = convertirAeventTypesEntity(dto);
            eventTypesEntity guardado = repo.save(entidad);
            return covertirAeventTypesDTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar el evento: {}", e.getMessage());
            throw new RuntimeException("Error interno al guardar el nuevo tipo de evento.");
        }
    }

    public eventTypesDTO modificarEvento(String id, eventTypesDTO dto) {
        eventTypesEntity eventoExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("El tipo de evento con ID " + id + " no fue encontrado."));

        eventoExistente.setUniversityID(dto.getUniversityID());
        eventoExistente.setTypeName(dto.getTypeName());

        eventTypesEntity actualizado = repo.save(eventoExistente);
        return covertirAeventTypesDTO(actualizado);
    }

    public boolean eliminarEvento(String id) {
        try {
            if (!repo.existsById(id)) {
                throw new ExceptionNoSuchElement("No se encontró ningún evento con el ID: " + id + " para ser eliminado.");
            }
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar el evento con ID: " + id + ". Detalle: " + e.getMessage());
        }
    }

    // Convertir de Entity a DTO
    private eventTypesDTO covertirAeventTypesDTO(eventTypesEntity enti) {
        eventTypesDTO dto = new eventTypesDTO();
        dto.setEventTypeID(enti.getEventTypeID());
        dto.setUniversityID(enti.getUniversityID());
        dto.setTypeName(enti.getTypeName());
        return dto;
    }

    // Convertir de DTO a Entity
    private eventTypesEntity convertirAeventTypesEntity(eventTypesDTO dto) {
        eventTypesEntity entity = new eventTypesEntity();
        entity.setUniversityID(dto.getUniversityID());
        entity.setTypeName(dto.getTypeName());
        return entity;
    }
}
