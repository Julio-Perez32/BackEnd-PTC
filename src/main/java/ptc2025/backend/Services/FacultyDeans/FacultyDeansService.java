package ptc2025.backend.Services.FacultyDeans;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.FacultyDeans.FacultyDeansEntity;
import ptc2025.backend.Models.DTO.FacultyDeans.FacultyDeansDTO;
import ptc2025.backend.Respositories.FacultyDeans.FacultyDeansRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FacultyDeansService {

    @Autowired
    private FacultyDeansRepository repo;


    public List<FacultyDeansDTO> obtenerDatos() {
        List<FacultyDeansEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private FacultyDeansDTO convertirADTO(FacultyDeansEntity facultyDeansEntity) {
        FacultyDeansDTO dto = new FacultyDeansDTO();
        dto.setId(facultyDeansEntity.getId());
        dto.setFacultyID(facultyDeansEntity.getFacultyID());
        dto.setEmployeeID(facultyDeansEntity.getEmployeeID());
        dto.setStartDate(facultyDeansEntity.getStartDate());
        dto.setEndDate(facultyDeansEntity.getEndDate());
        return dto;
    }

    public FacultyDeansDTO insertarDatos(FacultyDeansDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos incorrectos");
        }
        try {
            FacultyDeansEntity entity = convertirAEntity(data);
            FacultyDeansEntity registrosGuardado = repo.save(entity);
            return convertirADTO(registrosGuardado);
        }catch (Exception e){
            log.error("Error al registrar el nuevo dato" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el dato");
        }
    }

    private FacultyDeansEntity convertirAEntity(FacultyDeansDTO data) {
        FacultyDeansEntity entity = new FacultyDeansEntity();
        entity.setFacultyID(data.getFacultyID());
        entity.setEmployeeID(data.getEmployeeID());
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());
        return entity;
    }

    public FacultyDeansDTO actualizarDatos(String id, FacultyDeansDTO json) {
        FacultyDeansEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setFacultyID(json.getFacultyID());
        existente.setEmployeeID(json.getEmployeeID());
        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());
        FacultyDeansEntity RegistroActualizado = repo.save(existente);
        return convertirADTO(RegistroActualizado);
    }

    public boolean EliminarDatos(String id) {
        try {
            FacultyDeansEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el registro con el id" + id, 1);
        }
    }
}
