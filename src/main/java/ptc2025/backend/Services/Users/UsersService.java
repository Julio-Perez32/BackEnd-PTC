package ptc2025.backend.Services.Users;


import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ptc2025.backend.Config.Argon2.Argon2Password;
import ptc2025.backend.Entities.People.PeopleEntity;
import ptc2025.backend.Entities.Universities.UniversityEntity;
import ptc2025.backend.Entities.Users.UsersEntity;
import ptc2025.backend.Entities.systemRoles.SystemRolesEntity;
import ptc2025.backend.Exceptions.ExceptionNotFound;
import ptc2025.backend.Exceptions.ExceptionServerError;
import ptc2025.backend.Models.DTO.Universities.UniversityDTO;
import ptc2025.backend.Models.DTO.Users.UsersDTO;
import ptc2025.backend.Respositories.People.PeopleRepository;
import ptc2025.backend.Respositories.Universities.UniversityRespository;
import ptc2025.backend.Respositories.Users.UsersRespository;
import ptc2025.backend.Respositories.systemRoles.systemRolesRespository;
import ptc2025.backend.Services.Cloudinary.CloudinaryService;
import ptc2025.backend.Utils.EmailService;
import ptc2025.backend.Utils.PasswordGenerator;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
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

    public Cloudinary cloudinary;

    //Metodos para poder encriptar la contra y enviar el correo
    @Autowired
    private EmailService emailService;

    @Autowired
    private Argon2Password passwordHasher;
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private CloudinaryService cloudinaryService;


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
    public Page<UsersDTO> getUsersPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UsersEntity> pageEntity = repo.findAll(pageable);

        if (pageEntity.isEmpty()) {
            throw new ExceptionNotFound("No se encontraron definiciones de materias para la página solicitada.");
        }

        return pageEntity.map(this::convertirUsuarioADTO);
    }

    public UsersDTO convertirUsuarioADTO(UsersEntity usuario) {
        UsersDTO dto = new UsersDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setContrasena(usuario.getContrasena());
        dto.setImageUrlUser(usuario.getImageUrlUser());

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

    /**public UsersDTO insertarDatos(UsersDTO data) {
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
    }*/
    @Transactional
    public UsersDTO insertarDatos(UsersDTO data, MultipartFile file){
        try {
            if (data == null) {
                throw new IllegalArgumentException("El objeto usuario no puede ser nulo.");
            }
            if (data.getEmail() == null || data.getEmail().isEmpty()) {
                throw new IllegalArgumentException("El correo electrónico no puede ser nulo.");
            }
            if (data.getContrasena() == null || data.getContrasena().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
            }
            repo.findByEmail(data.getEmail()).ifPresent(user -> {
                throw new IllegalArgumentException("El correo electrónico ya está registrado.");
            });

            try{
                String imageUrl = null;
                if(file != null && !file.isEmpty()){
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
                    imageUrl = (String) uploadResult.get("secure_url");
                    data.setImageUrlUser(imageUrl);
                }
            }catch (IOException e){
                throw new IllegalArgumentException(e.getMessage());
            }

            // Siempre generamos una contraseña aleatoria
            String tempPassword = generatedRandomPassword();
            String passwordEncriptada = passwordHasher.EncryptPassword(tempPassword);

            // Crear la entidad

            UsersEntity entity = convertirAEntity(data);

            UsersEntity saved = repo.save(entity);

            UsersDTO response = convertirUsuarioADTO(saved);


            // Enviar correo con la contraseña temporal
            emailService.sendWelcomeEmail(response.getEmail(), response.getPersonName(), tempPassword);


            return response;
        }catch (Exception e){
            log.error("Error al registrar el usuario: " + e.getMessage(), e);
            throw new ExceptionServerError("Error interno al registrar el usuario");
        }



    }


    private UsersEntity convertirAEntity(UsersDTO data) {
        UsersEntity entity = new UsersEntity();
        entity.setEmail(data.getEmail());
        entity.setContrasena(data.getContrasena());
        entity.setImageUrlUser(data.getImageUrlUser());

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

            //si  la contra viene en el DTO, se encripta; si no, se deja igual
            if (json.getContrasena() != null && !json.getContrasena().isEmpty()) {
                existente.setContrasena(passwordHasher.EncryptPassword(json.getContrasena()));
            }

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
            UsersDTO dto = convertirUsuarioADTO(usuarioActualizado);
            dto.setContrasena(null); // no devolver hash
            return dto;

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
    private String generatedRandomPassword(){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(12); //Longitud de 12 caracteres

        for (int i = 0; i<12; i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}

