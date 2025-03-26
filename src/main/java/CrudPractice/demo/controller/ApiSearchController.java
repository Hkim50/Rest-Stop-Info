package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.*;
import CrudPractice.demo.service.ApiSearchService;
import CrudPractice.demo.service.MemberService2;
import CrudPractice.demo.service.ReviewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ApiSearchController {

    private final ApiSearchService apiSearchService;
    private final ReviewsService reviewsService;
    private final MemberService2 memberService2;

    public ApiSearchController(ApiSearchService apiSearchService, ReviewsService reviewsService, MemberService2 memberService2) {
        this.apiSearchService = apiSearchService;
        this.reviewsService = reviewsService;
        this.memberService2 = memberService2;
    }

    @GetMapping("/find")
    public String find(StoreFormDto storeFormDto, Model model) throws UnsupportedEncodingException {
        Optional<ApiListEntity> byName = apiSearchService.findByName(storeFormDto.getName());

        if (byName.isPresent()) {
            String encodedTitle = URLEncoder.encode(byName.get().getTitle(), "UTF-8");
            return "redirect:/api/" + encodedTitle;
        }

        ApiResponseDto store = apiSearchService.findStore(storeFormDto.getName());

        if (storeFormDto.getName().equals("") || store.getItems().size() == 0) {
            model.addAttribute("errorMessage", "음식점 이름을 다시 한번 확인해주세요.");
            return "/error/errorPage";
        }

        // 리뷰 갯수 가져오기
        List<Integer> numOfReviews = new ArrayList<>();

        store.getItems().stream().forEach(f -> {
            ApiListDto dto = apiSearchService.findByName(f.getTitle()).get().toDto();
            numOfReviews.add(reviewsService.findByApiList(dto).size());
        });

        List<ApiListDto> profPhoto = reviewsService.getProfPhoto(apiSearchService.findByName(store.getItems()));

        model.addAttribute("numReviews", numOfReviews);
        model.addAttribute("restaurants", profPhoto);
        model.addAttribute("size", store.getTotal());
        model.addAttribute("searchInput", storeFormDto.getName());

        return "search/resultList";
    }

    @GetMapping("/{title}")
    public String searchDetail(@PathVariable("title") String title,
                               @RequestParam(name = "sort", defaultValue = "latest") String sort, Model model) {

        ApiListDto dto = apiSearchService.findByName(title).get().toDto();
        List<ReviewsDto> byApiList = reviewsService.findByApiList(dto, sort);

        List<String> photos = reviewsService.getPhotos(byApiList);
        UserEntity user = memberService2.getUser();

        boolean hasImages = photos.size() > 0;

        model.addAttribute("name", user);
        model.addAttribute("reviews", byApiList);
        model.addAttribute("restaurant", dto);
        model.addAttribute("numReviews", byApiList.size());
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("photos", photos);
        model.addAttribute("currentSort", sort);

        return "search/storeInfo";
    }




}
