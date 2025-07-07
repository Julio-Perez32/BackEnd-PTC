package ptc2025.backend.Services.Universities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j /** Es una anotacion de loombook que sirve para mostrar mensajes en consola
 tipo ustedes le ponen log.error("lol, algo salio mal") o log.info("Todo correcto")
 y eso les va a ayudar a saber que esta pasando lo que pasa mientras corre el programa
 */
@Service
public class UniversityServices {

    //Inyectando el repositorio
    @Autowired
    UniversityRespository repo;

    public List<UniversityDTO> getUniversityService(){
        List<UniversityEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirAUniversityDTO)
                .collect(Collectors.toList());
    }

    public UniversityDTO insertarUniversidad(UniversityDTO dto){
        // Validaciones combinadas
        if (dto.getUniversityName() == null || dto.getUniversityName().isBlank() ||
                dto.getRector() == null || dto.getRector().isBlank() ||
                dto.getWebPage() == null || dto.getWebPage().isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos: nombre de universidad, rector y página web.");
        }
        // Validación de URL
        if (!dto.getWebPage().matches("^https?://.+")) {
            throw new IllegalArgumentException("La página web debe iniciar con http:// o https://");
        }
        try {
            // Convertir DTO → Entity
            UniversityEntity entidad = convertirAUniversityEntity(dto);

            // Guardar en BD
            UniversityEntity guardado = repo.save(entidad);

            // Convertir Entity → DTO
            return convertirAUniversityDTO(guardado);
        }catch (Exception e){
            log.error("Error al registrar una nueva universidad " + e.getMessage());
            throw new RuntimeException("Error interno al guardar la universidad");
        }

    }
    public UniversityDTO modificarUniversidad(String id, UniversityDTO dto){
        UniversityEntity universidadExistente = repo.findById(id).orElseThrow(() -> new RuntimeException("El dato no pudo ser actualizado. Universidad no encontrada"));
        //Actualizacion de los datos
        universidadExistente.setUniversityName(dto.getUniversityName());
        universidadExistente.setRector(dto.getRector());
        universidadExistente.setWebPage(dto.getWebPage());

        UniversityEntity actulizado = repo.save(universidadExistente);
        return convertirAUniversityDTO(actulizado);


    }
    public boolean elminarUniversidad (String id){
        try {
            //Validacion de existencia de Universidad
            UniversityEntity objUniversidad = repo.findById(id).orElse(null);
            //Si existe se procede a eliminar
            if (objUniversidad != null){
                repo.deleteById(id);
                return true;
            }else {
                System.out.println("Universidad no encontrada");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ninguna Universidad con el ID:" + id + "para poder ser eliminada", 1);
        }
    }
    //Conviertiendo los valores del Entity a la clase de DTO
    private UniversityDTO convertirAUniversityDTO(UniversityEntity university){
        UniversityDTO DTO = new UniversityDTO();
        DTO.setUniversityID(university.getUniversityID());
        DTO.setUniversityName(university.getUniversityName());
        DTO.setRector(university.getRector());
        DTO.setWebPage(university.getWebPage());
        return DTO;

    }
    //Convirtiendo DTO  a Entity
    private UniversityEntity convertirAUniversityEntity(UniversityDTO dto){
        UniversityEntity entity = new UniversityEntity();
        entity.setUniversityName(dto.getUniversityName());
        entity.setRector(dto.getRector());
        entity.setWebPage(dto.getWebPage());
        return entity;
    }

}
