package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.PrincipalDetails;
import CrudPractice.demo.dto.ReviewsDto;
import CrudPractice.demo.dto.UserDto;
import CrudPractice.demo.repository.ReviewsRepository;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserInfoController {

    private final MemberService2 memberService2;
    private final ReviewsService reviewsService;

    @Autowired
    public UserInfoController(MemberService2 memberService2, ReviewsService reviewsService) {
        this.memberService2 = memberService2;
        this.reviewsService = reviewsService;
    }

    @GetMapping("/info")
    public String info(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());

        UserDto userDto = byName.toDto();

        List<ReviewsDto> reviews = reviewsService.getReviewByUser(byName);

        double avg = 0.0;

        for (ReviewsDto review : reviews) {
            avg += review.getRating();
        }
        avg = avg / reviews.size();
        avg = Math.round(avg * 100.0) / 100.0;

        model.addAttribute("avg", avg);
        model.addAttribute("reviews", reviews);
        model.addAttribute("user", userDto);

        return "myInfo";
    }
}
