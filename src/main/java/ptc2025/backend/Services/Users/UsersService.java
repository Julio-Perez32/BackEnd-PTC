package ptc2025.backend.Services.Users;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;
import ptc2025.backend.Respositories.Users.UsersRespository;
import ptc2025.backend.Respositories.systemRoles.systemRolesRespository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UsersService {

    @Autowired
    private UsersRespository repo;

    @Autowired
    UniversityRespository repoUniversity;

    @Autowired
    systemRolesRespository repoSystemRoles;

    @Autowired
    PeopleRepository repoPeople;

    public List<UsersDTO> getAllUsers() {
        try {
            List<UsersEntity> users = repo.findAll();
            return users.stream()
                    .map(this::convertirUsuarioADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al obtener la lista de usuarios", e);
            throw new ExceptionServerError("Error interno al obtener la lista de usuarios");
        }
    }

    public UsersDTO convertirUsuarioADTO(UsersEntity usuario) {
        UsersDTO dto = new UsersDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setContrasena(usuario.getContrasena());

        // Universidad
        if (usuario.getUniversity() != null) {
            dto.setUniversityName(usuario.getUniversity().getUniversityName());
            dto.setUniversityID(usuario.getUniversity().getUniversityID());
        } else {
            dto.setUniversityName("Sin Universidad Asignada");
            dto.setUniversityID(null);
        }

        // Persona
        if (usuario.getPeople() != null) {
            dto.setPersonName(usuario.getPeople().getFirstName());
            dto.setPersonLastName(usuario.getPeople().getLastName());
            dto.setPersonId(usuario.getPeople().getPersonID());
        } else {
            dto.setPersonName("Sin Persona Asignada");
            dto.setPersonId(null);
        }

        // Rol
        if (usuario.getSystemRoles() != null) {
            dto.setRolesName(usuario.getSystemRoles().getRoleName());
            dto.setRoleId(usuario.getSystemRoles().getRoleId());
        } else {
            dto.setRolesName("Sin Rol Asignado");
            dto.setRoleId(null);
        }

        return dto;
    }

    public UsersDTO insertarDatos(UsersDTO data) {
        if (data == null || data.getContrasena() == null || data.getContrasena().isEmpty()) {
            throw new IllegalArgumentException("Usuario o contraseña no pueden ser nulos");
        }
        try {
            UsersEntity entity = convertirAEntity(data);
            UsersEntity usuarioGuardado = repo.save(entity);
            return convertirUsuarioADTO(usuarioGuardado);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar el usuario: " + e.getMessage(), e);
            throw new ExceptionServerError("Error interno al registrar el usuario");
        }
    }

    private UsersEntity convertirAEntity(UsersDTO data) {
        UsersEntity entity = new UsersEntity();
        entity.setEmail(data.getEmail());
        entity.setContrasena(data.getContrasena());

        // Universidad
        if (data.getUniversityID() != null) {
            UniversityEntity university = repoUniversity.findById(data.getUniversityID())
                    .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + data.getUniversityID()));
            entity.setUniversity(university);
        }

        // Persona
        if (data.getPersonId() != null) {
            PeopleEntity people = repoPeople.findById(data.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + data.getPersonId()));
            entity.setPeople(people);
        }

        // Rol
        if (data.getRoleId() != null) {
            SystemRolesEntity systemRoles = repoSystemRoles.findById(data.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Rol del sistema no encontrado con ID: " + data.getRoleId()));
            entity.setSystemRoles(systemRoles);
        }

        return entity;
    }

    // Actualizar
    public UsersDTO actualizarDatos(String id, UsersDTO json) {
        UsersEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionNotFound("Usuario no encontrado con el ID: " + id));

        try {
            existente.setEmail(json.getEmail());
            existente.setContrasena(json.getContrasena());

            // Universidad
            if (json.getUniversityID() != null) {
                UniversityEntity university = repoUniversity.findById(json.getUniversityID())
                        .orElseThrow(() -> new IllegalArgumentException("Universidad no encontrada con ID: " + json.getUniversityID()));
                existente.setUniversity(university);
            } else {
                existente.setUniversity(null);
            }

            // Persona
            if (json.getPersonId() != null) {
                PeopleEntity person = repoPeople.findById(json.getPersonId())
                        .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada con ID: " + json.getPersonId()));
                existente.setPeople(person);
            } else {
                existente.setPeople(null);
            }

            // Rol
            if (json.getRoleId() != null) {
                SystemRolesEntity systemRoles = repoSystemRoles.findById(json.getRoleId())
                        .orElseThrow(() -> new IllegalArgumentException("Rol del sistema no encontrado con ID: " + json.getRoleId()));
                existente.setSystemRoles(systemRoles);
            } else {
                existente.setSystemRoles(null);
            }

            UsersEntity usuarioActualizado = repo.save(existente);
            return convertirUsuarioADTO(usuarioActualizado);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar el usuario con ID: " + id, e);
            throw new ExceptionServerError("Error interno al actualizar el usuario");
        }
    }

    public boolean eliminarUsuario(String id) {
        try {
            UsersEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                throw new ExceptionNotFound("Usuario no encontrado con el ID: " + id);
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionNotFound("No se encontró el usuario para eliminar con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al eliminar usuario con ID: " + id, e);
            throw new ExceptionServerError("Error interno al eliminar el usuario");
        }
    }

    public Optional<UsersDTO> findById(String id) {
        try {
            Optional<UsersEntity> entity = repo.findById(id);
            return entity.map(this::convertirUsuarioADTO);
        } catch (Exception e) {
            log.error("Error interno al buscar usuario por ID: " + id, e);
            throw new ExceptionServerError("Error interno al buscar usuario por ID");
        }
    }
}

