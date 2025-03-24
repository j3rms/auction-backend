package auction.entities.DTO;

import auction.entities.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private Role role;
}
