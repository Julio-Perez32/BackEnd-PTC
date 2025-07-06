package ptc2025.backend.Services.Universities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Respositories.Universities.UniversityRespository;

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
    private UniversityRespository repo;

    public List<UniversityDTO> getUniversityService(){
        List<UniversityEntity> universidad = repo.findAll();
        return universidad.stream()
                .map(this::convertirAUniversityDTO)
                .collect(Collectors.toList());
    }
    /**
     * Conviertiendo los valores del Entity a la clase de DTO
     * */
    private UniversityDTO convertirAUniversityDTO(UniversityEntity university){
        UniversityDTO DTO = new UniversityDTO();
        DTO.setUniversityID(university.getUniversityID());
        DTO.setUniversityName(university.getUniversityName());
        DTO.setRector(university.getRector());
        DTO.setWebPage(university.getWebPage());
        return DTO;

    }
}
