package CrudPractice.demo.controller;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.PrincipalDetails;
import CrudPractice.demo.dto.ReviewsDto;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.RestBestFoodService;
import CrudPractice.demo.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
public class ReviewController {
    private final ReviewsService reviewsService;
    private final MemberService2 memberService2;
    private final RestBestFoodService restBestFoodService;

    @Autowired
    public ReviewController(ReviewsService reviewsService, MemberService2 memberService2, RestBestFoodService restBestFoodService) {
        this.reviewsService = reviewsService;
        this.memberService2 = memberService2;
        this.restBestFoodService = restBestFoodService;
    }


    @PostMapping("/addReview")
    public String addReview(ReviewsDto reviewsDto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());

        ReviewsEntity reviewsEntity = reviewsService.addUserInReview(reviewsDto, byName);
        RestInfoEntity infoFromDb = restBestFoodService.getInfoFromDb(reviewsDto.getRestNm());

        reviewsEntity.setRestInfoEntity(infoFromDb);

        reviewsService.addReview(reviewsEntity);
        model.addAttribute("name", reviewsDto.getName());

        return "/search/findRest";
    }

    @PostMapping("/edit")
    public String showEditForm(ReviewsDto review, Model model) {
        System.out.println(review.getCreatedAt());
        System.out.println(review.getName());
        System.out.println(review.getRating());

        model.addAttribute("review", review);

        return "/editForm";
    }

    @PostMapping("/update")
    public String updateReview(ReviewsDto reviewsDto, Model model) {
        ReviewsEntity reviewByNameAndCreatedAt = reviewsService.getReviewByNameAndCreatedAt(reviewsDto.getName(), reviewsDto.getCreatedAt());

        reviewByNameAndCreatedAt.setContent(reviewsDto.getContent());
        reviewByNameAndCreatedAt.setRating(reviewsDto.getRating());

        reviewsService.addReview(reviewByNameAndCreatedAt);

        model.addAttribute("name", reviewsDto.getName());

        return "redirect:/find";
    }

    @PostMapping("/delete")
    public String deleteReview(ReviewsDto reviewsDto, Model model) {
        reviewsService.removeReview(reviewsDto);

        return "redirect:/find";
    }
}
