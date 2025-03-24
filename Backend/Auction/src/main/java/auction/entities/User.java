package auction.entities;

import auction.entities.RO.UserRO;
import auction.entities.enums.Role;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateFromRO(UserRO userRO) {
        this.name = userRO.getName();
        this.email = userRO.getEmail();
        this.password = userRO.getPassword();
        this.role = userRO.getRole();
    }
}
