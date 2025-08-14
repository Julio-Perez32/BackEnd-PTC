package ptc2025.backend.Respositories.systemRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.systemRoles.systemRolesEntity;

@Repository
public interface systemRolesRespository extends JpaRepository<systemRolesEntity, String> {
}
