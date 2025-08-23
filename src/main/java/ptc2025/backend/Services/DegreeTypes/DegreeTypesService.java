package ptc2025.backend.Services.DegreeTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Models.DTO.DegreeTypes.DegreeTypesDTO;
import ptc2025.backend.Respositories.DegreeTypes.DegreeTypesRepository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DegreeTypesService {

    @Autowired
    private DegreeTypesRepository repo;

    public List<DegreeTypesDTO> getAllDegreeTypes(){
        List<DegreeTypesEntity> degreetype = repo.findAll();
        return degreetype.stream()
                .map(this::hacerdegreestypeADTO)
                .collect(Collectors.toList());
    }

    public DegreeTypesDTO insertarDatos(DegreeTypesDTO data) {
        if(data == null){
            throw new IllegalArgumentException("Datos no validos");
        }
        try{
            DegreeTypesEntity entity = convertirAEntity(data);
            DegreeTypesEntity degreeTypeGuardado = repo.save(entity);
            return hacerdegreestypeADTO(degreeTypeGuardado);
        }catch (Exception e){
            log.error("Error al registrar el degreeType" + e.getMessage());
            throw new RuntimeException("Error");
        }
    }

    private DegreeTypesEntity convertirAEntity(DegreeTypesDTO data){
        DegreeTypesEntity entity = new DegreeTypesEntity();
        entity.setUniversityID(data.getUniversityID());
        entity.setDegreeTypeName(data.getDegreeTypeName());
        return entity;
    }

    public DegreeTypesDTO hacerdegreestypeADTO(DegreeTypesEntity degreeT) {

        DegreeTypesDTO dto = new DegreeTypesDTO();
        dto.setId(degreeT.getId());
        dto.setUniversityID(degreeT.getUniversityID());
        dto.setDegreeTypeName(degreeT.getDegreeTypeName());
        return dto;
    }

    public DegreeTypesDTO actualizarDegreeType(String id, DegreeTypesDTO json) {
        DegreeTypesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("DegreeType not found"));
        existente.setUniversityID(json.getUniversityID());
        existente.setDegreeTypeName(json.getDegreeTypeName());
        DegreeTypesEntity degreeTypeAtualizado = repo.save(existente);
        return hacerdegreestypeADTO(degreeTypeAtualizado);
    }

    public boolean eliminarDegreeType(String id) {
        try{
            DegreeTypesEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }
            else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el DegreeType", 1);
        }
    }
}
