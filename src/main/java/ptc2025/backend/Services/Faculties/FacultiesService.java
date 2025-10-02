package ptc2025.backend.Services.Faculties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.Faculties.FacultiesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacultiesService {

    @Autowired
    FacultiesRepository repo;

    public List<FacultiesDTO> getAllFaculties() {
        List<FacultiesEntity> faculties = repo.findAll();
        if (faculties.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen facultades registradas.");
        }
        return faculties.stream()
                .map(this::convertToFacultiesDTO)
                .collect(Collectors.toList());
    }

    public Page<FacultiesDTO> getFacultiesPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FacultiesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen facultades registradas para la página solicitada.");
        }

        return pageEntity.map(this::convertToFacultiesDTO);
    }

    public FacultiesDTO convertToFacultiesDTO(FacultiesEntity faculties) {
        FacultiesDTO dto = new FacultiesDTO();
        dto.setFacultyID(faculties.getFacultyID());
        dto.setFacultyName(faculties.getFacultyName());
        dto.setFacultyCode(faculties.getFacultyCode());
        dto.setContactPhone(faculties.getContactPhone());
        dto.setCorrelativeCode(faculties.getCorrelativeCode());
        return dto;
    }

    public FacultiesEntity convertToFacultiesEntity(FacultiesDTO dto) {
        FacultiesEntity entity = new FacultiesEntity();
        entity.setFacultyID(dto.getFacultyID());
        entity.setFacultyName(dto.getFacultyName());
        entity.setFacultyCode(dto.getFacultyCode());
        entity.setContactPhone(dto.getContactPhone());
        entity.setCorrelativeCode(dto.getCorrelativeCode());
        return entity;
    }

    public FacultiesDTO insertFaculty(FacultiesDTO dto) {
        if (dto == null ||
                dto.getFacultyName() == null || dto.getFacultyName().isEmpty() ||
                dto.getFacultyCode() == null || dto.getFacultyCode().isEmpty() ||
                dto.getContactPhone() == null || dto.getContactPhone().isEmpty() ||
                dto.getCorrelativeCode() == null || dto.getCorrelativeCode().isEmpty()) {

            throw new ExceptionBadRequest("Todos los campos de la facultad son obligatorios.");
        }

        try {
            FacultiesEntity entity = convertToFacultiesEntity(dto);
            FacultiesEntity facultySaved = repo.save(entity);
            return convertToFacultiesDTO(facultySaved);
        } catch (Exception e) {
            log.error("Error al registrar la facultad: {}", e.getMessage());
            throw new RuntimeException("Error interno al registrar la facultad.");
        }
    }

    public FacultiesDTO updateFaculty(String id, FacultiesDTO json) {
        FacultiesEntity existsFaculty = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Facultad con ID " + id + " no encontrada."));

        existsFaculty.setFacultyName(json.getFacultyName());
        existsFaculty.setFacultyCode(json.getFacultyCode());
        existsFaculty.setContactPhone(json.getContactPhone());
        existsFaculty.setCorrelativeCode(json.getCorrelativeCode());

        FacultiesEntity updatedFaculty = repo.save(existsFaculty);
        return convertToFacultiesDTO(updatedFaculty);
    }

    public boolean deleteFaculty(String id) {
        try {
            if (!repo.existsById(id)) {
                throw new ExceptionNoSuchElement("No se encontró la facultad con ID: " + id + " para eliminar.");
            }
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar la facultad con ID: " + id + ". Detalle: " + e.getMessage());
        }
    }
}
