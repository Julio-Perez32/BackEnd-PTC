package ptc2025.backend.Services.CycleTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CycleTypes.CycleTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.CycleTypes.CycleTypesDTO;
import ptc2025.backend.Respositories.CycleTypes.CycleTypesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CycleTypesService {

    @Autowired
    private CycleTypesRepository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;


    public List<CycleTypesDTO> getAllCycleTypes(){
        List<CycleTypesEntity> cycletype = repo.findAll();
        return cycletype.stream()
                .map(this::ConvertCycleTypeDTO)
                .collect(Collectors.toList());
    }

    public CycleTypesDTO insertarCycleType(CycleTypesDTO data) {
        if(data == null){
            throw new IllegalArgumentException("los datos no pueden ser nulos");
        }
        try{
            CycleTypesEntity entity = convertirAEntity(data);
            CycleTypesEntity cycleTypeGuardado = repo.save(entity);
            return ConvertCycleTypeDTO(cycleTypeGuardado);
        }catch (Exception e){
            log.error("Error al ingresar el tipo de ciclo" + e.getMessage());
            throw new RuntimeException("error interno");
        }
    }

    public CycleTypesDTO ConvertCycleTypeDTO(CycleTypesEntity cycleType){
        CycleTypesDTO dto = new CycleTypesDTO();
        dto.setId(cycleType.getId());
        dto.setCycleLabel(cycleType.getCycleLabel());

        if(cycleType.getUniversity() != null){
            dto.setUniversityName(cycleType.getUniversity().getUniversityName());
            dto.setUniversityID(cycleType.getUniversity().getUniversityID());
        }else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }


    private CycleTypesEntity convertirAEntity(CycleTypesDTO data) {
        CycleTypesEntity entity = new CycleTypesEntity();
        entity.setId(data.getId());
        entity.setCycleLabel(data.getCycleLabel());

        if(data.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }


        return entity;
    }


    public CycleTypesDTO actualizarCycleTypes(String id, CycleTypesDTO json) {
        CycleTypesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("CyleType not found"));
        existente.setCycleLabel(json.getCycleLabel());

        if(json.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        }else {
            existente.setUniversity(null);
        }
        CycleTypesEntity cycleTypeActualizado = repo.save(existente);
        return ConvertCycleTypeDTO(cycleTypeActualizado);
    }

    public boolean eliminarCycleType(String id) {
        try{
            CycleTypesEntity existente = repo.findById(id).orElse(null);
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
