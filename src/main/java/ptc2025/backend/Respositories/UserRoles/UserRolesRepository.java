package ptc2025.backend.Respositories.UserRoles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptc2025.backend.Entities.UserRoles.UserRolesEntity;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRolesEntity, String>{
}
