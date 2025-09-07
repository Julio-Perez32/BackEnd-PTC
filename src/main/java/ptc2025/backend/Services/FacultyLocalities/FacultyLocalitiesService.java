package ptc2025.backend.Services.FacultyLocalities;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EvaluationInstruments.EvaluationInstrumentsEntity;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.FacultyLocalities.FacultyLocalitiesEntity;
import ptc2025.backend.Entities.Localities.LocalitiesEntity;
import ptc2025.backend.Exceptions.ExceptionBadRequest;
import ptc2025.backend.Exceptions.ExceptionNoSuchElement;
import ptc2025.backend.Models.DTO.FacultyLocalities.FacultyLocalitiesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;
import ptc2025.backend.Respositories.FacultyLocalities.FacultyLocalitiesRepository;
import ptc2025.backend.Respositories.Localities.LocalitiesRespository;

import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class FacultyLocalitiesService {

    @Autowired
    FacultyLocalitiesRepository repo;

    @Autowired
    FacultiesRepository facultyRepo;

    @Autowired
    LocalitiesRespository localitiesRespo;

    public List<FacultyLocalitiesDTO> getAllFacultyLocalities() {
        List<FacultyLocalitiesEntity> facultyLocalities = repo.findAll();

        if (facultyLocalities.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen localidades de facultades registradas.");
        }

        return facultyLocalities.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    public Page<FacultyLocalitiesDTO> getFacultiesLocalitiesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacultyLocalitiesEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNoSuchElement("No existen registros en la página solicitada.");
        }

        return pageEntity.map(this::ConvertirADTO);
    }

    private FacultyLocalitiesDTO ConvertirADTO(FacultyLocalitiesEntity facultyLocalities) {
        FacultyLocalitiesDTO dto = new FacultyLocalitiesDTO();
        dto.setId(facultyLocalities.getId());

        if (facultyLocalities.getFaculty() != null) {
            dto.setFaculties(facultyLocalities.getFaculty().getFacultyName());
            dto.setFacultyID(facultyLocalities.getFaculty().getFacultyID());
        } else {
            dto.setFaculties("Sin Facultad Asignada");
            dto.setFacultyID(null);
        }

        if (facultyLocalities.getLocalities() != null) {
            dto.setLocalities(facultyLocalities.getLocalities().getAddress());
            dto.setLocalityID(facultyLocalities.getLocalities().getLocalityID());
        } else {
            dto.setLocalities("Sin Localidad Asignada");
            dto.setLocalityID(null);
        }

        return dto;
    }

    public FacultyLocalitiesDTO insertarDatos(FacultyLocalitiesDTO data) {
        if (data == null) {
            throw new ExceptionBadRequest("Datos no encontrados. El cuerpo de la solicitud no puede estar vacío.");
        }

        try {
            FacultyLocalitiesEntity entity = convertirAEntity(data);
            FacultyLocalitiesEntity datoGuardado = repo.save(entity);
            return ConvertirADTO(datoGuardado);
        } catch (Exception e) {
            log.error("Error al registrar el dato: {}", e.getMessage());
            throw new RuntimeException("Error interno al registrar el nuevo dato.");
        }
    }

    private FacultyLocalitiesEntity convertirAEntity(FacultyLocalitiesDTO data) {
        FacultyLocalitiesEntity entity = new FacultyLocalitiesEntity();
        entity.setId(data.getId());

        if (data.getFacultyID() != null) {
            FacultiesEntity faculty = facultyRepo.findById(data.getFacultyID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada con ID: " + data.getFacultyID()));
            entity.setFaculty(faculty);
        }

        if (data.getLocalityID() != null) {
            LocalitiesEntity locality = localitiesRespo.findById(data.getLocalityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Localidad no encontrada con ID: " + data.getLocalityID()));
            entity.setLocalities(locality);
        }

        return entity;
    }

    public FacultyLocalitiesDTO actualizarDatos(String id, FacultyLocalitiesDTO json) {
        FacultyLocalitiesEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNoSuchElement("Registro no encontrado con ID: " + id));

        if (json.getFacultyID() != null) {
            FacultiesEntity faculty = facultyRepo.findById(json.getFacultyID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Facultad no encontrada con ID: " + json.getFacultyID()));
            existente.setFaculty(faculty);
        } else {
            existente.setFaculty(null);
        }

        if (json.getLocalityID() != null) {
            LocalitiesEntity locality = localitiesRespo.findById(json.getLocalityID())
                    .orElseThrow(() -> new ExceptionNoSuchElement("Localidad no encontrada con ID: " + json.getLocalityID()));
            existente.setLocalities(locality);
        } else {
            existente.setLocalities(null);
        }

        FacultyLocalitiesEntity actualizado = repo.save(existente);
        return ConvertirADTO(actualizado);
    }

    public boolean eliminarDato(String id) {
        try {
            FacultyLocalitiesEntity existente = repo.findById(id).orElse(null);

            if (existente == null) {
                throw new ExceptionNoSuchElement("No se encontró el registro con el ID: " + id);
            }

            repo.deleteById(id);
            return true;

        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNoSuchElement("Error al intentar eliminar el registro con ID: " + id + ". Detalle: " + e.getMessage());
        }
    }
}
