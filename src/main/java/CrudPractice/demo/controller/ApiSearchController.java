package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.ReviewsEntity;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<ApiListEntity> entities = apiSearchService.findByString(store);
        List<ReviewsEntity> reviews = reviewsService.findByApiList(entities);

        List<ApiListDto> dtos = reviewsService.getProfPhoto(entities, reviews);

        Map<ApiListEntity, Long> reviewCountMap = reviews.stream()
                .collect(Collectors.groupingBy(ReviewsEntity::getApiListEntity, Collectors.counting()));

        dtos.stream().forEach(f -> {
            f.setNumOfReviews(reviewCountMap.getOrDefault(f.toEntity(), 0L));
        });


        model.addAttribute("restaurants", dtos);
        model.addAttribute("searchInput", storeFormDto.getName());

        return "search/resultList";
    }

    @GetMapping("/{title}")
    public String searchDetail(@PathVariable("title") String title,
                               @RequestParam(name = "sort", defaultValue = "latest") String sort, Model model) {

        List<ReviewsEntity> reviews = reviewsService.findByApiList(title, sort);

        ApiListDto dto;
        if (reviews.size() == 0) {
            dto = apiSearchService.findByName(title).get().toDto();
        }
        else {
            dto = reviews.get(0).getApiListEntity().toDto();
        }

        List<String> photos = reviewsService.getPhotos2(reviews);
        UserEntity user = memberService2.getUser();
        boolean hasImages = photos.size() > 0;

        model.addAttribute("name", user);
        model.addAttribute("reviews", reviews);
        model.addAttribute("restaurant", dto);
        model.addAttribute("numReviews", reviews.size());
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("photos", photos);
        model.addAttribute("currentSort", sort);

        return "search/storeInfo";
    }




}
