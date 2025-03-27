package auction.entities.RO;

import auction.entities.User;
import auction.entities.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Role role;

    public User toEntity() {
        return User.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .username(this.username)
                .email(this.email)
                .password(this.password)
                .role(this.role)
                .build();
    }
}