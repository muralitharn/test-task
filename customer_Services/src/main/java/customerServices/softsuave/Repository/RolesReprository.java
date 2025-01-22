package customerServices.softsuave.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import customerServices.softsuave.newModel.Roles;

import java.util.UUID;

public interface RolesReprository extends JpaRepository<Roles, UUID> {
    Roles  findByRoles(String role);
}
