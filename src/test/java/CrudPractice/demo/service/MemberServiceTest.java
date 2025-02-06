package CrudPractice.demo.service;

import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.UserDto;
import CrudPractice.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired PasswordEncoder passwordEncoder;

    @Test
    public void test1() {
        UserDto userDto = new UserDto("Kevin", "qwer@gmail.com", "qwerqwer");
        UserEntity userEntity = memberService.saveMember(userDto);

        assertThat(userDto.getEmail()).isEqualTo(userEntity.getEmail());

    }

    @Test
    public void test2() {

    }

}
