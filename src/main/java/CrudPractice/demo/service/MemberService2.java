package CrudPractice.demo.service;

import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.PrincipalDetails;
import CrudPractice.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserEntity getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return userRepository.findByEmail(principalDetails.getUserEmail());
    }

    public Long getUserCount() {
        return userRepository.count();
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
