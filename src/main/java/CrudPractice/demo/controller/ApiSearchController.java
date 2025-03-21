package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.*;
import CrudPractice.demo.service.ApiSearchService;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ApiSearchController {

    private final ApiSearchService apiSearchService;
    private final ReviewsService reviewsService;
    private final MemberService2 memberService2;

    public ApiSearchController(ApiSearchService apiSearchService,ReviewsService reviewsService, MemberService2 memberService2) {
        this.apiSearchService = apiSearchService;
        this.reviewsService = reviewsService;
        this.memberService2 = memberService2;
    }

    @GetMapping("/api/find")
    public String find(StoreFormDto storeFormDto, Model model) {
        Optional<ApiListEntity> byName = apiSearchService.findByName(storeFormDto.getName());

        if (byName.isPresent()) {
            ApiListDto dto = byName.get().toDto();
            model.addAttribute("restaurant", dto);
            return "search/storeInfo";
        }

        ApiResponseDto store = apiSearchService.findStore(storeFormDto.getName());

        if (storeFormDto.getName().equals("") || store.getItems().size() == 0) {
            model.addAttribute("errorMessage", "음식점 이름을 다시 한번 확인해주세요.");
            return "/error/errorPage";
        }

        // 스팟에 리뷰 갯수 가져오기
        List<Integer> numOfReviews = new ArrayList<>();

        store.getItems().stream().forEach(f -> {
            ApiListDto dto = apiSearchService.findByName(f.getTitle()).get().toDto();
            numOfReviews.add(reviewsService.findByApiList(dto).size());
        });

        // resultList 가져올때 store 의 프로필 사진 가져와야함.
        List<String> profPhotos = new ArrayList<>();
        List<ApiListEntity> entity = apiSearchService.findByName(store.getItems());

        List<ApiListDto> profPhoto = reviewsService.getProfPhoto(entity);

        model.addAttribute("numReviews", numOfReviews);
        model.addAttribute("restaurants", profPhoto);
        model.addAttribute("size", store.getTotal());
        model.addAttribute("searchInput", storeFormDto.getName());


        return "search/resultList";

    }
    @GetMapping("/api/{title}")
    public String searchDetail(@PathVariable("title") String title, Model model) {
        ApiListDto dto =  apiSearchService.findByName(title).get().toDto();
        List<ReviewsDto> byApiList = reviewsService.findByApiList(dto);
        List<String> photos = reviewsService.getPhotos(byApiList);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());

        boolean hasImages = photos.size() > 0;


        model.addAttribute("name", byName);
        model.addAttribute("reviews", byApiList);
        model.addAttribute("restaurant", dto);
        model.addAttribute("numReviews", byApiList.size());
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("photos", photos);

        return "search/storeInfo";
    }
}
