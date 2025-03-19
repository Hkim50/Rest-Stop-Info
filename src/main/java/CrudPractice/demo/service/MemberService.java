package CrudPractice.demo.service;

import CrudPractice.demo.dto.PrincipalDetails;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.UserDto;
import CrudPractice.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("가입된 계정이 없습니다: " + email);
        }

        // check if user trying to log in with Oauth2 info
        if (user.getProviderId() != null) {
            throw new RuntimeException("OAuth2 회원입니다. 소셜 로그인을 사용해주세요.");
        }

        return new PrincipalDetails(user);
    }

    public UserEntity saveMember(UserDto userDto) {
        // check if email is already in the db
        validateDuplicate(userDto.getEmail());
        UserEntity userEntity = new UserEntity(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), userDto.getName());

        return repository.save(userEntity);
    }

    private void validateDuplicate(String email) {
        UserEntity user = repository.findByEmail(email);
        if (user != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


}
