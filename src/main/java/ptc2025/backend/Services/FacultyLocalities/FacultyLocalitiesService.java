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
        return facultyLocalities.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    public Page<FacultyLocalitiesDTO> getFacultiesLocalitiesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacultyLocalitiesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::ConvertirADTO);
    }

    private FacultyLocalitiesDTO ConvertirADTO(FacultyLocalitiesEntity facultyLocalities) {
        FacultyLocalitiesDTO dto = new FacultyLocalitiesDTO();
        dto.setId(facultyLocalities.getId());
        if(facultyLocalities.getFaculty() != null){
            dto.setFaculties(facultyLocalities.getFaculty().getFacultyName());
            dto.setFaculties(facultyLocalities.getFaculty().getFacultyID());
        }else {
            dto.setFaculties("Sin Facultad Asignada");
            dto.setFaculties(null);
        }
        if(facultyLocalities.getLocalities() != null){
            dto.setLocalities(facultyLocalities.getLocalities().getAddress());
            dto.setLocalityID(facultyLocalities.getLocalities().getLocalityID());
        }else {
            dto.setFaculties("Sin Localidad Asignada");
            dto.setFaculties(null);
        }
        return dto;
    }

    public FacultyLocalitiesDTO insertarDatos(FacultyLocalitiesDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("Datos no encontrados");
        }
        try {
            FacultyLocalitiesEntity entity = convertirAEntity(data);
            FacultyLocalitiesEntity datoGuardado = repo.save(entity);
            return ConvertirADTO(datoGuardado);
        }catch (Exception e){
            log.error("Error al registrar el dato" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }
    }

    private FacultyLocalitiesEntity convertirAEntity(FacultyLocalitiesDTO data) {
        FacultyLocalitiesEntity entity = new FacultyLocalitiesEntity();
        entity.setId(data.getId());
        if(data.getFaculties() != null){
            FacultiesEntity Faculty = facultyRepo.findById(data.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Facultad no encontrada con ID: " + data.getId()));
            entity.setFaculty(Faculty);
        }
        if(data.getLocalities() != null){
            LocalitiesEntity localities = localitiesRespo.findById(data.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada con ID: " + data.getId()));
            entity.setLocalities(localities);
        }
        return entity;
    }

    public FacultyLocalitiesDTO actualizarDatos(String id, FacultyLocalitiesDTO json) {
        FacultyLocalitiesEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        FacultyLocalitiesEntity datoActualizado = repo.save(existente);
        if(json.getFacultyID() != null){
            FacultiesEntity faculties = facultyRepo.findById(json.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getFaculties()));
            existente.setFaculty(faculties);
        }else {
            existente.setFaculty(null);
        }
        if(json.getLocalityID() != null){
            LocalitiesEntity localities = localitiesRespo.findById(json.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getLocalities()));
            existente.setLocalities(localities);
        }else {
            existente.setFaculty(null);
        }
        return ConvertirADTO(datoActualizado);
    }

    public boolean eliminarDato(String id) {
        try {
            FacultyLocalitiesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro", 1);
        }
    }
}
