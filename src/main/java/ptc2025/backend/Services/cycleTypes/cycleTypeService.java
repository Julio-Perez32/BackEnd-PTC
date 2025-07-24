package ptc2025.backend.Services.cycleTypes;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.cycleTypes.cycleTypesEntity;
import ptc2025.backend.Models.DTO.cycleTypes.cycleTypesDTO;
import ptc2025.backend.Respositories.cycleTypes.cycleTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class cycleTypeService {

    @Autowired
    private cycleTypeRepository repo;

    public List<cycleTypesDTO> getAllCycleTypes(){
        List<cycleTypesEntity> cycletype = repo.findAll();
        return cycletype.stream()
                .map(this::ConvertCycleTypeDTO)
                .collect(Collectors.toList());
    }

    public cycleTypesDTO insertarCycleType(cycleTypesDTO data) {
        if(data == null){
            throw new IllegalArgumentException("los datos no pueden ser nulos");
        }
        try{
            cycleTypesEntity entity = convertirAEntity(data);
            cycleTypesEntity cycleTypeGuardado = repo.save(entity);
            return ConvertCycleTypeDTO(cycleTypeGuardado);
        }catch (Exception e){
            log.error("Error al ingresar el tipo de ciclo" + e.getMessage());
            throw new RuntimeException("error interno");
        }
    }

    public cycleTypesDTO ConvertCycleTypeDTO(cycleTypesEntity cycleType){
        cycleTypesDTO dto = new cycleTypesDTO();
        dto.setId(cycleType.getId());
        dto.setUniversityID(cycleType.getUniversityID());
        dto.setCycleLabel(cycleType.getCycleLabel());
        return dto;
    }


    private cycleTypesEntity convertirAEntity(cycleTypesDTO data) {
        cycleTypesEntity entity = new cycleTypesEntity();
        entity.setId(data.getId());
        entity.setUniversityID(data.getUniversityID());
        entity.setCycleLabel(data.getCycleLabel());
        return entity;
    }


    public cycleTypesDTO actualizarCycleTypes(String id, cycleTypesDTO json) {
        cycleTypesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("CyleType not found"));
        existente.setUniversityID(json.getUniversityID());
        existente.setCycleLabel(json.getCycleLabel());
        cycleTypesEntity cycleTypeActualizado = repo.save(existente);
        return ConvertCycleTypeDTO(cycleTypeActualizado);
    }

    public boolean eliminarCycleType(String id) {
        try{
            cycleTypesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el CycleType", 1);
        }
    }
}
