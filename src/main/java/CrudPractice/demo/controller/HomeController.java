package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
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

        HashMap<ApiListEntity, Integer> map = new HashMap<>();
        List<ApiListEntity> all = apiSearchService.findAll();

        all.stream().forEach(f -> {
            List<ReviewsDto> byApiList = reviewsService.findByApiList(f.toDto());
            map.put(f, byApiList.size());
        });

        // 리뷰 수 기준으로 정렬 후 상위 3개만 추출
        List<ApiListEntity> sortedSpots = map.entrySet().stream()
                .sorted(Map.Entry.<ApiListEntity, Integer>comparingByValue().reversed()) // 내림차순
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<ApiListDto> topSpots = reviewsService.getProfPhoto(sortedSpots);

        topSpots.stream().forEach(f ->{
            f.setNumOfReviews(map.get(f.toEntity()).intValue());
        });


        model.addAttribute("topSpots", topSpots);
        return "newhome";
    }
}
