package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.dto.ReviewsDto;
import CrudPractice.demo.service.ApiSearchService;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.RestBestFoodService;
import CrudPractice.demo.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final MemberService2 memberService2;
    private final ApiSearchService apiSearchService;
    private final ReviewsService reviewsService;

    @Autowired
    public HomeController(MemberService2 memberService2, ApiSearchService apiSearchService, ReviewsService reviewsService) {
        this.memberService2 = memberService2;
        this.apiSearchService = apiSearchService;
        this.reviewsService = reviewsService;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<ApiListEntity> all = apiSearchService.findAll();
        List<ReviewsEntity> allReviews = reviewsService.findByApiList(all);

        Map<ApiListEntity, Long> reviewCountMap = allReviews.stream()
                .collect(Collectors.groupingBy(ReviewsEntity::getApiListEntity, Collectors.counting()));

        // 정렬 후 상위 3개 추출
        List<ApiListEntity> sortedSpots = reviewCountMap.entrySet().stream()
                .sorted(Map.Entry.<ApiListEntity, Long>comparingByValue().reversed()) // 내림차순 정렬
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<ApiListDto> topSpots = reviewsService.getProfPhoto(sortedSpots, allReviews);

        // DTO에 리뷰 개수 설정
        topSpots.forEach(dto -> dto.setNumOfReviews(reviewCountMap.getOrDefault(dto.toEntity(), 0L)));
        // 리뷰 평균 rating 설정
        topSpots.forEach(dto -> dto.setAvgRating(reviewsService.getAvg(allReviews.stream()
                .filter(review -> review.getApiListEntity()
                        .equals(dto.toEntity()))
                .collect(Collectors.toList())
                .stream()
                .map(ReviewsEntity::toDto).toList())));

        model.addAttribute("topSpots", topSpots);
        return "newhome";
    }

    @GetMapping("/intro")
    public String intro() {
        return "intro";
    }
}
