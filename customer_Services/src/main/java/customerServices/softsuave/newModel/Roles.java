package customerServices.softsuave.newModel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "customer_role")
@AllArgsConstructor
@NoArgsConstructor
public class Roles {

    @Id
    private UUID roleId ;


    @Column(name = "roles")
    private String roles;
}
