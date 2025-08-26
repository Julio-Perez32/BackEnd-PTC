package ptc2025.backend.Services.YearCycles;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.YearCycles.YearCyclesEntity;
import ptc2025.backend.Models.DTO.YearCycles.YearCyclesDTO;
import ptc2025.backend.Respositories.YearCycles.YearCyclesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class YearCyclesService {

    @Autowired
    YearCyclesRepository repo;

    public List<YearCyclesDTO> obtenerRegistros() {
        List<YearCyclesEntity> Lista = repo.findAll();
        return Lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private YearCyclesDTO convertirADTO(YearCyclesEntity yearCyclesEntity) {
        YearCyclesDTO dto = new YearCyclesDTO();
        dto.setId(yearCyclesEntity.getId());
        dto.setAcademicYearID(yearCyclesEntity.getAcademicYearID());
        dto.setCycleTypeID(yearCyclesEntity.getCycleTypeID());
        dto.setStartDate(yearCyclesEntity.getStartDate());
        dto.setEndDate(yearCyclesEntity.getEndDate());
        return dto;
    }

    public YearCyclesDTO insertarDatos(YearCyclesDTO data) {
        if (data == null){
            throw new IllegalArgumentException("Datos no correctoss");
        }
        try {
            YearCyclesEntity entity = convertirAEntity(data);
            YearCyclesEntity registroGuardado = repo.save(entity);
            return convertirADTO(registroGuardado);
        } catch (Exception e) {
            log.error("Error al querer guardar los datos ingresados" + e.getMessage());
            throw new IllegalArgumentException("Error al registrar el nuevo dato");
        }
    }

    private YearCyclesEntity convertirAEntity(YearCyclesDTO data) {
        YearCyclesEntity entity = new YearCyclesEntity();
        entity.setAcademicYearID(data.getAcademicYearID());
        entity.setCycleTypeID(data.getCycleTypeID());
        entity.setStartDate(data.getStartDate());
        entity.setEndDate(data.getEndDate());
        return entity;
    }

    public YearCyclesDTO ActualizarRegistro(String id, YearCyclesDTO json) {
        YearCyclesEntity existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Registro no encontrado"));
        existente.setAcademicYearID(json.getAcademicYearID());
        existente.setCycleTypeID(json.getCycleTypeID());
        existente.setStartDate(json.getStartDate());
        existente.setEndDate(json.getEndDate());
        YearCyclesEntity RegistroActualizado = repo.save(existente);
        return convertirADTO(RegistroActualizado);
    }

    public boolean removerRegistro(String id) {
        try{
            YearCyclesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el  registro", 1);
        }
    }
}
