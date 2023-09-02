package physicks.secondBoard.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private User(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static User of(String name, String email) {
        return new User(name, email, Role.USER);
    }

    public static User ofGuest(String name) {
        return new User(name, "", Role.GUEST);
    }

    public User update(String name) {
        this.name = name;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
