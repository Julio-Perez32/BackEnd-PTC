package ptc2025.backend.Services.Users;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;
import ptc2025.backend.Respositories.Users.UsersRespository;
import ptc2025.backend.Respositories.systemRoles.systemRolesRespository;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UsersService {

    @Autowired
    private UsersRespository repo;

    @Autowired //Se inyecta el repositorio de University
    UniversityRespository repoUniversity;

    @Autowired //Se inyecta el repositorio de University
    systemRolesRespository repoSystemRoles;

    @Autowired
    PeopleRepository repoPeople;

    public List<UsersDTO> getAllUsers(){
        List<UsersEntity> users = repo.findAll();
        return users.stream()
                .map(this::convertirUsuarioADTO)
                .collect(Collectors.toList());
    }

    public UsersDTO convertirUsuarioADTO(UsersEntity usuario){
        UsersDTO dto = new UsersDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setContrasena(usuario.getContrasena());
        //para convertir a DTO el id de la universidad del usuario
        if(usuario.getUniversity() != null){
            dto.setUniversityName(usuario.getUniversity().getUniversityName());
            dto.setUniversityID(usuario.getUniversity().getUniversityID());
        }else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }
        if(usuario.getPeople() != null){
            dto.setPersonName(usuario.getPeople().getFirstName());
            dto.setPersonLastName(usuario.getPeople().getLastName());
            dto.setPersonId(usuario.getPeople().getPersonID());
        }else {
            dto.setPersonName("Sin Persona Asignada");
            dto.setPersonId(null);
        }
        //para convertir a DTO el rol del sistema del usuario
        if(usuario.getSystemRoles() != null){
            dto.setRolesName(usuario.getSystemRoles().getRoleName());
            dto.setRoleId(usuario.getSystemRoles().getRoleId());
        }else {
            dto.setRolesName("Sin Rol Asignado");
            dto.setRoleId(null);
        }
        return dto;
    }

    public UsersDTO insertarDatos(UsersDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()){
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos");
        }
        try {
            UsersEntity entity = convertirAEntity(data);
            UsersEntity usuarioGuardado = repo.save(entity);
            return convertirUsuarioADTO(usuarioGuardado);
        }catch (Exception e){
           log.error("Error al registrar el usuario" + e.getMessage());
           throw new RuntimeException("Error al registrar el usuario");
        }
    }


    private UsersEntity convertirAEntity(UsersDTO data){
        UsersEntity entity = new UsersEntity();
        entity.setEmail(data.getEmail());
        entity.setContrasena(data.getContrasena());
        //Para obtener el id de universidad
        if(data.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }
        if(data.getPersonId() != null){
            PeopleEntity people = repoPeople.findById(data.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + data.getPersonId()));
            entity.setPeople(people);
        }
        //para obtener el id rol del sistema
        if(data.getRoleId() != null){
            SystemRolesEntity systemRoles = repoSystemRoles.findById(data.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol del sistema no encontrada con ID: " + data.getRoleId()));
            entity.setSystemRoles(systemRoles);
        }

        return entity;
    }

    // Actualizar
    public UsersDTO actualizarDatos(String id, UsersDTO json) {
        UsersEntity existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setEmail(json.getEmail());
        existente.setContrasena(json.getContrasena());

        // Universidad
        if(json.getUniversityID() != null){
            UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getUniversityID()));
            existente.setUniversity(university);
        } else {
            existente.setUniversity(null);
        }
        // Persona asociada
        if (json.getPersonId() != null){
            PeopleEntity person = repoPeople.findById(json.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID proporcionado: " + json.getPersonId()));
            existente.setPeople(person);
        }else {
            existente.setPeople(null);
        }
        // Rol en el sistema
        if(json.getRoleId() != null){
            SystemRolesEntity systemRoles = repoSystemRoles.findById(json.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol del sistema no encontrado con ID proporcionado: " + json.getRoleId()));
            existente.setSystemRoles(systemRoles);
        } else {
            existente.setSystemRoles(null);
        }


        UsersEntity usuarioActualizado = repo.save(existente);
        return convertirUsuarioADTO(usuarioActualizado);
    }


    public boolean eliminarUsuario(String id) {
        try {
            UsersEntity existente = repo.findById(id).orElse(null);
            if (existente != null){
                repo.deleteById(id);
                return true;
            }else {
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el usuario", 1);
        }
    }
}

