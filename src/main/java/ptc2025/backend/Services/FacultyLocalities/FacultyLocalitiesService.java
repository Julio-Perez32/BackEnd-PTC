package ptc2025.backend.Services.FacultyLocalities;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EvaluationInstruments.EvaluationInstrumentsEntity;
import ptc2025.backend.Entities.FacultyLocalities.FacultyLocalitiesEntity;
import ptc2025.backend.Models.DTO.FacultyLocalities.FacultyLocalitiesDTO;
import ptc2025.backend.Respositories.FacultyLocalities.FacultyLocalitiesRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FacultyLocalitiesService {

    @Autowired
    FacultyLocalitiesRepository repo;


    public List<FacultyLocalitiesDTO> getAllFacultyLocalities() {
        List<FacultyLocalitiesEntity> facultyLocalities = repo.findAll();
        return facultyLocalities.stream()
                .map(this::ConvertirADTO)
                .collect(Collectors.toList());
    }

    private FacultyLocalitiesDTO ConvertirADTO(FacultyLocalitiesEntity facultyLocalities) {
        FacultyLocalitiesDTO dto = new FacultyLocalitiesDTO();
        dto.setId(facultyLocalities.getId());
        dto.setFacultyID(facultyLocalities.getFacultyID());
        dto.setLocalityID(facultyLocalities.getFacultyID());
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
        entity.setLocalityID(data.getLocalityID());
        entity.setFacultyID(data.getFacultyID());
        return entity;
    }

    public FacultyLocalitiesDTO actualizarDatos(String id, FacultyLocalitiesDTO json) {
        FacultyLocalitiesEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        existente.setFacultyID(json.getFacultyID());
        existente.setLocalityID(json.getLocalityID());
        FacultyLocalitiesEntity datoActualizado = repo.save(existente);
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
