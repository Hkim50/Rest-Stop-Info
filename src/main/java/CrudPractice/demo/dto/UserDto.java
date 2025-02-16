package CrudPractice.demo.dto;

import CrudPractice.demo.domain.UserEntity;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public UserEntity toEntity() {
        return UserEntity.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}
