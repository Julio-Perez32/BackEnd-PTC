package ptc2025.backend.Services.facultyCorrelatives;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Faculties.FacultiesEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;
import ptc2025.backend.Models.DTO.facultyCorrelatives.facultyCorrelativesDTO;
import ptc2025.backend.Respositories.Faculties.FacultiesRepository;
import ptc2025.backend.Respositories.facultyCorrelatives.facultyCorrelativesRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class facultyCorrelativesService {
    @Autowired
    facultyCorrelativesRespository repo;

    @Autowired
    private FacultiesRepository repoFaculties;

    public List<facultyCorrelativesDTO> getFacultyCorrelatives(){
        List<facultyCorrelativesEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Page<facultyCorrelativesDTO> getFacultiesCorrelativesPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<facultyCorrelativesEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }
    public facultyCorrelativesDTO insertFacultyCorrelatives(facultyCorrelativesDTO dto){
        // Validaciones combinadas
        if (dto.getFacultyID() == null || dto.getFacultyID().isBlank() ||
                dto.getCorrelativeNumber() == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos: nombre de la facultad y el correlativo.");
        }
        try {
            // Convertir DTO → Entity
            facultyCorrelativesEntity entidad = convertirAEntity(dto);

            // Guardar en BD
            facultyCorrelativesEntity guardado = repo.save(entidad);

            // Convertir Entity → DTO
            return convertirADTO(guardado);
        }catch (Exception e){
            log.error("Error al registrar el correlativo " + e.getMessage());
            throw new RuntimeException("Error interno al guardar el correlativo");
        }

    }
    public facultyCorrelativesDTO updateFacultyCorrelatives(String id, facultyCorrelativesDTO dto){
        facultyCorrelativesEntity correlativo = repo.findById(id).orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Correlativo no encontrada"));
        //Actualizacion de los datos
        correlativo.setCorrelativeNumber(dto.getCorrelativeNumber());
        if(dto.getCorrelativeID() != null){
            FacultiesEntity faculties = repoFaculties.findById(dto.getCorrelativeID())
                    .orElseThrow(()-> new IllegalArgumentException("Facultad no encontrada"));
        }else {
            correlativo.setFaculty(null);
        }

        facultyCorrelativesEntity actulizado = repo.save(correlativo);
        return convertirADTO(actulizado);


    }
    public boolean deleteFacultyCorrelatives (String id){
        try {
            //Validacion de existencia de Universidad
            facultyCorrelativesEntity objUniversidad = repo.findById(id).orElse(null);
            //Si existe se procede a eliminar
            if (objUniversidad != null){
                repo.deleteById(id);
                return true;
            }else {
                System.out.println("Correlativo no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ninguna correlativo con el ID:" + id + "para poder ser eliminada", 1);
        }
    }
    private facultyCorrelativesDTO convertirADTO (facultyCorrelativesEntity entity){
        facultyCorrelativesDTO dto = new facultyCorrelativesDTO();
        dto.setCorrelativeID(entity.getCorrelativeID());
        dto.setCorrelativeNumber(entity.getCorrelativeNumber());
        if(entity.getFaculty() != null){
            dto.setFacultyName(entity.getFaculty().getFacultyName());
            dto.setFacultyName(entity.getFaculty().getFacultyID());
        }else {
            dto.setFacultyName("Sin Facultad aisgnada");
            dto.setFacultyID(null);
        }
        return dto;
    }
    private facultyCorrelativesEntity convertirAEntity(facultyCorrelativesDTO dto){
        facultyCorrelativesEntity entity = new facultyCorrelativesEntity();
        entity.setCorrelativeNumber(dto.getCorrelativeNumber());
        if(dto.getCorrelativeID() != null){
            FacultiesEntity faculties = repoFaculties.findById(dto.getCorrelativeID())
                    .orElseThrow(()-> new IllegalArgumentException("Facultad no encontrada"));
        }
        return entity;


    }
}
