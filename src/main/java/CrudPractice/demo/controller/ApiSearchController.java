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


        model.addAttribute("restaurants", store.getItems());
        model.addAttribute("size", store.getTotal());


        return "search/resultList";

    }
    @GetMapping("/api/{title}")
    public String searchDetail(@PathVariable("title") String title, Model model) {
        ApiListDto dto =  apiSearchService.findByName(title).get().toDto();
        List<ReviewsDto> byApiList = reviewsService.findByApiList(dto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());

        model.addAttribute("name", byName);
        model.addAttribute("reviews", byApiList);
        model.addAttribute("restaurant", dto);
        return "search/storeInfo";
    }
}
