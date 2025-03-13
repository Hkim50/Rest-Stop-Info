package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.PrincipalDetails;
import CrudPractice.demo.dto.ReviewsDto;
import CrudPractice.demo.service.ApiSearchService;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.RestBestFoodService;
import CrudPractice.demo.service.ReviewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewHandler {
    private final ReviewsService reviewsService;
    private final MemberService2 memberService2;
    private final RestBestFoodService restBestFoodService;
    private final ApiSearchService apiSearchService;

    @Autowired
    public ReviewHandler(ReviewsService reviewsService, MemberService2 memberService2, RestBestFoodService restBestFoodService, ApiSearchService apiSearchService) {
        this.reviewsService = reviewsService;
        this.memberService2 = memberService2;
        this.restBestFoodService = restBestFoodService;
        this.apiSearchService = apiSearchService;
    }

    // CREATE
    @PostMapping("/save")
    public ResponseEntity<ReviewsEntity> save(@RequestBody ReviewsDto reviewsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());

        ReviewsEntity reviewsEntity = reviewsService.addUserInReview(reviewsDto, byName);

        if (reviewsDto.getRestNm().contains("휴게소")) {
            reviewsEntity.setRestInfoEntity(restBestFoodService.getInfoFromDb(reviewsDto.getRestNm()));
        }
        else {
            reviewsEntity.setApiListEntity(apiSearchService.findByName(reviewsDto.getRestNm()).get());
        }

        reviewsService.addReview(reviewsEntity);

        return ResponseEntity.ok(reviewsEntity);
    }

    // DELETE
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity delete(@PathVariable("reviewId") Long id) {
        reviewsService.deleteReview(id);
        return ResponseEntity.ok(id);
    }


}
