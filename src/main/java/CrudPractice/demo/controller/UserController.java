package CrudPractice.demo.controller;

import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.UserDto;
import CrudPractice.demo.dto.UserFormDto;
import CrudPractice.demo.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    private final MemberService memberService;

    @Autowired
    public UserController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login() {

        return "newLogin";
    }

//    @PostMapping("/login")
//    public String userLogin(@Valid UserFormDto userFormDto, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            return "/login";
//        }
//        memberService.loadUserByUsername(userFormDto.getEmail());
//
//        return "/";
//    }


    @GetMapping("/register")
    public String register() {

        return "/newRegister";
    }

    @PostMapping("/register")
    public String userRegister(@Valid UserFormDto userFormDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("Error occurred");
            return "/register";
        }

        UserDto userDto = new UserDto(userFormDto.getName(), userFormDto.getEmail(), userFormDto.getPassword());
        memberService.saveMember(userDto);

        return "/login";
    }
}
