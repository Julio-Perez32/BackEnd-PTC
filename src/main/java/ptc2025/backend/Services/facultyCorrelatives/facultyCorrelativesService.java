package ptc2025.backend.Services.facultyCorrelatives;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.facultyCorrelatives.facultyCorrelativesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;
import ptc2025.backend.Respositories.facultyCorrelatives.facultyCorrelativesRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class facultyCorrelativesService {

    @Autowired
    facultyCorrelativesRespository repo;

    @Autowired
    private FacultiesRepository repoFaculties;

    public List<facultyCorrelativesDTO> getFacultyCorrelatives() {
        List<facultyCorrelativesEntity> correlativos = repo.findAll();

        if (correlativos.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen correlativos registrados.");
        }

        return correlativos.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<facultyCorrelativesDTO> getFacultiesCorrelativesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<facultyCorrelativesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen correlativos registrados en la página solicitada.");
        }

        return pageEntity.map(this::convertirADTO);
    }

    public facultyCorrelativesDTO insertFacultyCorrelatives(facultyCorrelativesDTO dto) {
        if (dto.getFacultyID() == null || dto.getFacultyID().isBlank() ||
                dto.getCorrelativeNumber() == null) {
            throw new ExceptionBadRequest("Todos los campos obligatorios deben estar completos: nombre de la facultad y el correlativo.");
        }

        try {
            facultyCorrelativesEntity entidad = convertirAEntity(dto);

            facultyCorrelativesEntity guardado = repo.save(entidad);

            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar el correlativo {}", e.getMessage());
            throw new RuntimeException("Error interno al guardar el correlativo");
        }
    }

    public facultyCorrelativesDTO updateFacultyCorrelatives(String id, facultyCorrelativesDTO dto) {
        facultyCorrelativesEntity correlativo = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("El correlativo con ID " + id + " no fue encontrado."));

        correlativo.setCorrelativeNumber(dto.getCorrelativeNumber());

        if (dto.getCorrelativeID() != null) {
            repoFaculties.findById(dto.getCorrelativeID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada para el correlativo."));
        } else {
            correlativo.setFaculty(null);
        }

        facultyCorrelativesEntity actualizado = repo.save(correlativo);
        return convertirADTO(actualizado);
    }

    public boolean deleteFacultyCorrelatives(String id) {
        try {
            if (!repo.existsById(id)) {
                throw new ExceptionNoSuchElement("No se encontró correlativo con ID: " + id + " para poder eliminar.");
            }

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar el correlativo con ID: " + id + ". Detalle: " + e.getMessage());
        }
    }

    private facultyCorrelativesDTO convertirADTO(facultyCorrelativesEntity entity) {
        facultyCorrelativesDTO dto = new facultyCorrelativesDTO();
        dto.setCorrelativeID(entity.getCorrelativeID());
        dto.setCorrelativeNumber(entity.getCorrelativeNumber());

        if (entity.getFaculty() != null) {
            dto.setFacultyName(entity.getFaculty().getFacultyName());
            dto.setFacultyID(entity.getFaculty().getFacultyID());
        } else {
            dto.setFacultyName("Sin Facultad asignada");
            dto.setFacultyID(null);
        }
        return dto;
    }

    private facultyCorrelativesEntity convertirAEntity(facultyCorrelativesDTO dto) {
        facultyCorrelativesEntity entity = new facultyCorrelativesEntity();
        entity.setCorrelativeNumber(dto.getCorrelativeNumber());

        if (dto.getCorrelativeID() != null) {
            repoFaculties.findById(dto.getCorrelativeID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada para asignar correlativo."));
        }

        return entity;
    }
}
