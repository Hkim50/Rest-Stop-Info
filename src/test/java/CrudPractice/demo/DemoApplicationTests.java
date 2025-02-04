package CrudPractice.demo;

import CrudPractice.demo.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	public void save(){
		UserDto userDto = new UserDto(1L, "Kevin", "qwer@gmail.com");


	}
}
