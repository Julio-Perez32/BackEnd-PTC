package ptc2025.backend.Services.CodeGenerators;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.CodeGenerators.CodeGeneratorsEntity;
import ptc2025.backend.Entities.EntityType.EntityTypesEntity;
import ptc2025.backend.Entities.facultyCorrelatives.facultyCorrelativesEntity;
import ptc2025.backend.Exceptions.ExceptionLevelNotValid;
import ptc2025.backend.Models.DTO.AcademicLevel.AcademicLevelsDTO;
import ptc2025.backend.Models.DTO.CodeGenerators.CodeGeneratorsDTO;
import ptc2025.backend.Respositories.CodeGenerators.CodeGeneratorsRespository;
import ptc2025.backend.Respositories.EntityType.EntityTypesRepository;
import ptc2025.backend.Respositories.facultyCorrelatives.facultyCorrelativesRespository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeGeneratorsService {

    @Autowired
    CodeGeneratorsRespository repo;

    @Autowired
    EntityTypesRepository repoEntityType;

    @Autowired
    facultyCorrelativesRespository repoCorrelatives;

    public List<CodeGeneratorsDTO> getCodeGenerator(){
        List<CodeGeneratorsEntity> code = repo.findAll();
        return code.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CodeGeneratorsDTO insertCodeGenerastor(@Valid CodeGeneratorsDTO dto){
        if(dto == null){
            throw new IllegalArgumentException("Todos los campos deben estar completados.");
        }
        try{
            CodeGeneratorsEntity entity = convertToEntity(dto) ;
            CodeGeneratorsEntity save = repo.save(entity);
            return convertToDTO(save);
        }catch (Exception e){
            log.error("No se pudo registrar el estandar para la generacion de codigos " + e.getMessage());
            throw new ExceptionLevelNotValid("Error interno al guardar el estandar de generacion de codigos");
        }
    }

    public CodeGeneratorsDTO updateCodeGenerator(String id, @Valid CodeGeneratorsDTO dto){
        CodeGeneratorsEntity exist = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("El ID no pudo ser encontrado."));
        exist.setPrefix(dto.getPrefix());
        exist.setSuffixLength(dto.getSuffixLength());
        exist.setLastAssignedNumber(dto.getSuffixLength());

        if(dto.getEntityTypeID() != null){
            EntityTypesEntity entityTypes = repoEntityType.findById(dto.getEntityTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de entidad no encontrada con ID: " + dto.getEntityTypeID()));
            exist.setEntityType(entityTypes);
        }else {
            exist.setEntityType(null);
        }

        if(dto.getCorrelativeID() != null){
            facultyCorrelativesEntity correlatives = repoCorrelatives.findById(dto.getCorrelativeID())
                    .orElseThrow(() -> new IllegalArgumentException("Correlativo de la universidad no encontrada con ID: " + dto.getCorrelativeID()));
            exist.setFacultyCorrelative(correlatives);
        }else {
            exist.setFacultyCorrelative(null);
        }

        CodeGeneratorsEntity update = repo.save(exist);
        return convertToDTO(update);
    }

    public boolean  deleteCode(String id){
        try{
          CodeGeneratorsEntity exist = repo.findById(id).orElse(null);
          if (exist != null){
              repo.deleteById(id);
              return true;
          }else {
              return false;
          }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se pudo encontrar un estandar de codigo con el id: " + id + " para eliminar",1);
        }
    }
    public CodeGeneratorsEntity convertToEntity (CodeGeneratorsDTO dto){
        CodeGeneratorsEntity entity = new CodeGeneratorsEntity();
        entity.setPrefix(dto.getPrefix());
        entity.setSuffixLength(dto.getSuffixLength());
        entity.setLastAssignedNumber(dto.getLastAssignedNumber());
        if(dto.getEntityTypeID() != null){
            EntityTypesEntity entityTypes = repoEntityType.findById(dto.getEntityTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de entidad no encontrada con ID: " + dto.getEntityTypeID()));
            entity.setEntityType(entityTypes);
        }
        if(dto.getEntityTypeID() != null){
            EntityTypesEntity entityTypes = repoEntityType.findById(dto.getEntityTypeID())
                    .orElseThrow(() -> new IllegalArgumentException("Correlativo de la universidad no encontrada con ID: " + dto.getEntityTypeID()));
            entity.setEntityType(entityTypes);
        }
        return entity;
    }

    public CodeGeneratorsDTO convertToDTO(CodeGeneratorsEntity entity){
        CodeGeneratorsDTO dto = new CodeGeneratorsDTO();
        dto.setGeneratorID(entity.getGeneratorID());
        dto.setPrefix(entity.getPrefix());
        dto.setSuffixLength(entity.getSuffixLength());
        dto.setLastAssignedNumber(entity.getLastAssignedNumber());
        if(entity.getEntityType() != null){
            dto.setEntityTypeName(entity.getEntityType().getEntityType());
            dto.setEntityTypeID(entity.getEntityType().getEntityTypeID());
        }
        if (entity.getFacultyCorrelative() != null) {
            dto.setFacultyCorrelativesID(entity.getFacultyCorrelative().getCorrelativeID());
            dto.setFacultyCorrelativesName(
                    String.valueOf(entity.getFacultyCorrelative().getCorrelativeNumber())
            );
        }

        return dto;
    }


}

