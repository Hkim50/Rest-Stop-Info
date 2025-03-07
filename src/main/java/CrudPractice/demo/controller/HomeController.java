package CrudPractice.demo.controller;

import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.RestBestFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MemberService2 memberService2;
    @Autowired
    public HomeController(MemberService2 memberService2) {
        this.memberService2 = memberService2;
    }

    @GetMapping("/")
    public String home(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("name", name);
        return "newhome";
    }
}
