package ptc2025.backend.Services.DegreeTypes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.DegreeTypes.DegreeTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.DegreeTypes.DegreeTypesDTO;
import ptc2025.backend.Respositories.DegreeTypes.DegreeTypesRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class DegreeTypesService {

    @Autowired
    private DegreeTypesRepository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    public List<DegreeTypesDTO> getAllDegreeTypes(){
        List<DegreeTypesEntity> degreetype = repo.findAll();
        return degreetype.stream()
                .map(this::hacerdegreestypeADTO)
                .collect(Collectors.toList());
    }

    public Page<DegreeTypesDTO> getAllDegreeTypesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<DegreeTypesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::hacerdegreestypeADTO);
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
        entity.setDegreeTypeName(data.getDegreeTypeName());

        if(data.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        return entity;
    }

    public DegreeTypesDTO hacerdegreestypeADTO(DegreeTypesEntity degreeT) {

        DegreeTypesDTO dto = new DegreeTypesDTO();
        dto.setId(degreeT.getId());
        dto.setDegreeTypeName(degreeT.getDegreeTypeName());

        if(degreeT.getUniversity() != null){
            dto.setUniversityName(degreeT.getUniversity().getUniversityName());
            dto.setUniversityID(degreeT.getUniversity().getUniversityID());
        }else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        return dto;
    }

    public DegreeTypesDTO actualizarDegreeType(String id, DegreeTypesDTO json) {
        DegreeTypesEntity existente = repo.findById(id).orElseThrow(() -> new RuntimeException("DegreeType not found"));
        existente.setDegreeTypeName(json.getDegreeTypeName());
        if(json.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        }else {
            existente.setUniversity(null);
        }
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
