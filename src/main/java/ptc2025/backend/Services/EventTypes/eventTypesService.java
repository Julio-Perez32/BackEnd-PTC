package ptc2025.backend.Services.EventTypes;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.EventTypes.eventTypesEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.EventTypes.eventTypesDTO;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Respositories.EventTypes.eventTypesRespository;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class eventTypesService {
    @Autowired
    eventTypesRespository repo;

    //get
    public List<eventTypesDTO> getEventTypes(){
        List<eventTypesEntity> eventos = repo.findAll();
        return eventos.stream()
                .map(this::covertirAeventTypesDTO)
                .collect(Collectors.toList());
    }

    public eventTypesDTO insertarEvento(eventTypesDTO dto){
        //Validacion de datos vacios
        if (dto.getUniversityID() == null || dto.getUniversityID().isBlank() ||
                dto.getTypeName() == null || dto.getTypeName().isBlank()){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        try{
            eventTypesEntity entidad = convertirAeventTypesEntity(dto);
            eventTypesEntity guardado = repo.save(entidad);
            return covertirAeventTypesDTO(guardado);
        }catch (Exception e){
            log.error("Error al registrar el evento " + e.getMessage());
            throw new RuntimeException("Eror interno al guardar el nuevo evento");
        }
    }

    public eventTypesDTO modificarEvento(String id, eventTypesDTO dto){
        eventTypesEntity eventoExistente = repo.findById(id).orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Evento no encontrad")) ;
        eventoExistente.setUniversityID(dto.getUniversityID());
        eventoExistente.setTypeName(dto.getTypeName());

        eventTypesEntity actualizado = repo.save(eventoExistente);
        return covertirAeventTypesDTO(actualizado);
    }
    public boolean eliminarEvento (String id){
        try{
            eventTypesEntity objEvento = repo.findById(id).orElse(null);
            if (objEvento != null){
                repo.deleteById(id);
                return true;
            }else {
                System.out.println("Evento no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ningún evento con el ID: " + id + "para podeer ser eliminado", 1);

        }
    }
    //Convirtiendo los valores del Entity a la clase de DTO
    private eventTypesDTO covertirAeventTypesDTO(eventTypesEntity enti) {
        eventTypesDTO dto = new eventTypesDTO();
        dto.setEventTypeID(enti.getEventTypeID());
        dto.setUniversityID(enti.getUniversityID());
        dto.setTypeName(enti.getTypeName());
        return dto;
    }
    //Convirtiendo DTO  a Entity
    private eventTypesEntity convertirAeventTypesEntity(eventTypesDTO dto){
        eventTypesEntity entity = new eventTypesEntity();
        entity.setUniversityID(dto.getUniversityID());
        entity.setTypeName(dto.getTypeName());
        return entity;
    }
}
