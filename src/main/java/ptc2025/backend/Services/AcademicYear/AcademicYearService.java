package ptc2025.backend.Services.AcademicYear;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.AcademicLevel.AcademicLevelsEntity;
import ptc2025.backend.Entities.AcademicYear.AcademicYearEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.AcademicLevel.AcademicLevelsDTO;
import ptc2025.backend.Models.DTO.AcademicYear.AcademicYearDTO;
import ptc2025.backend.Respositories.AcademicYear.AcademicYearRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AcademicYearService {
    @Autowired
    private AcademicYearRepository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    //Get
    public List<AcademicYearDTO> getAcademicYear(){
        List<AcademicYearEntity> plan = repo.findAll();
        return plan.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());

    }

    public Page<AcademicYearDTO> getAcademicYearPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<AcademicYearEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    //POST
    public AcademicYearDTO insertAcademicYear(AcademicYearDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Los campos deben de ir completos");
        }
        try{
            AcademicYearEntity entity = convertirAEntity(dto);
            AcademicYearEntity save =    repo.save(entity);
            return convertirADTO(save);
        }
        catch(Exception e){
            throw new RuntimeException("No se pudo crear año academico " + e.getMessage() );
        }
    }

    //PUT
    public AcademicYearDTO updateAcademicYear(String id, AcademicYearDTO dto){
        try{
            if(repo.existsById(id)){
                AcademicYearEntity entity = repo.getById(id);

                entity.setYear(dto.getYear());
                entity.setStartDate(dto.getStartDate());
                entity.setEndDate(dto.getEndDate());
                entity.setCycleCount(dto.getCycleCount());
                entity.setAlowInterCycle(dto.getAllowInterCycle());
                entity.setDefaultCycleDuration(dto.getDefaultInterCycle());

                if(dto.getUniversityId() != null){
                    UniversityEntity university = repoUniversity.findById(dto.getUniversityId())
                            .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityId()));
                    entity.setUniversity(university);
                }else {
                    entity.setUniversity(null);
                }

                AcademicYearEntity save = repo.save(entity);
                return convertirADTO(save);
            }
            throw new IllegalArgumentException("El año academico con el id " + id + " no pudo ser actualizado");
        }
        catch (Exception e){
            throw new RuntimeException("No se pudo actualizar el año academico " + e.getMessage() );
        }
    }

    //DELETE

    public boolean deleteAcademicYear(String id){
        try{
            if(repo.existsById(id)){
                repo.deleteById(id);
                return true;
            }
            else{
                return false;
            }
        }
        catch(EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("El año academico con el id " + id + " no existe", 1);
        }
    }

    //Convertir a DTO
    private AcademicYearDTO convertirADTO(AcademicYearEntity entity){
        AcademicYearDTO dto = new AcademicYearDTO();
        dto.setAcademicYearId(entity.getAcademicYearId());
        dto.setYear(entity.getYear());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCycleCount(entity.getCycleCount());
        dto.setAllowInterCycle(entity.getAlowInterCycle());
        dto.setDefaultInterCycle(entity.getDefaultCycleDuration());

        if(entity.getUniversity() != null){
            dto.setUniversityName(entity.getUniversity().getUniversityName());
            dto.setUniversityId(entity.getUniversity().getUniversityID());
        }else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityId(null);
        }
        return dto;
    }

    private AcademicYearEntity convertirAEntity(AcademicYearDTO dto){
        AcademicYearEntity entity = new AcademicYearEntity();
        entity.setAcademicYearId(dto.getAcademicYearId());
        entity.setYear(dto.getYear());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setCycleCount(dto.getCycleCount());
        entity.setAlowInterCycle(dto.getAllowInterCycle());
        entity.setDefaultCycleDuration(dto.getDefaultInterCycle());

        if(dto.getUniversityId() != null){
            UniversityEntity university = repoUniversity.findById(dto.getUniversityId())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + dto.getUniversityId()));
            entity.setUniversity(university);
        }

        return entity;
    }
}
