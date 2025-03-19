package CrudPractice.demo.domain;

import CrudPractice.demo.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String providerId;

    public UserEntity(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.USER;
    }

    // overloading
    public UserEntity(String email, String password, String name, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = Role.USER;
        this.providerId = providerId;
    }


    public UserDto toDto() {
        return UserDto.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }

}
