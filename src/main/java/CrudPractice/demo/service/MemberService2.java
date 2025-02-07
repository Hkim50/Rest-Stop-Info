package CrudPractice.demo.service;

import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService2 {

    private final UserRepository userRepository;

    @Autowired
    public MemberService2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserByName(String name) {
        return userRepository.findByName(name);
    }
}
