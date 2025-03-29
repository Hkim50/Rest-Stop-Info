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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<ReviewsEntity> save(@ModelAttribute ReviewsDto reviewsDto,
                                              @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        UserEntity user = memberService2.getUser();

        reviewsService.addImage(image, reviewsDto);

        ReviewsEntity reviewsEntity = reviewsService.addUserInReview(reviewsDto, user);

        if (reviewsDto.getRestNm().contains("휴게소")) {
            reviewsEntity.setRestInfoEntity(restBestFoodService.getInfoFromDb(reviewsDto.getRestNm()));
        } else {
            reviewsEntity.setApiListEntity(apiSearchService.findByName(reviewsDto.getRestNm()).get());
        }

        reviewsService.addReview(reviewsEntity);

        return ResponseEntity.ok(reviewsEntity);
    }

    // DELETE
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Long> delete(@PathVariable("reviewId") Long id) {
        reviewsService.deleteReview(id);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/modify")
    public ResponseEntity modify(@ModelAttribute ReviewsDto dto,
                                 @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        ReviewsDto reviewsDto = reviewsService.addImage(image, dto);

        boolean isUpdated = reviewsService.updateReview(reviewsDto);
        if (isUpdated) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
