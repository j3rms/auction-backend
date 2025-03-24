package auction.entities.RO;

import auction.entities.enums.Role;
import auction.entities.User;
import lombok.Data;

@Data
public class UserRO {
    private String name;
    private String email;
    private String password;
    private Role role;

    public User toEntity() {
        User user = new User();
        user.setName(this.name);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }
}
