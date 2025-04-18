package CrudPractice.demo.controller;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.*;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchInfoController {
    private final RestBestFoodService restBestFoodService;
    private final ReviewsService reviewsService;
    private final MemberService2 memberService2;
    @Autowired
    public SearchInfoController(RestBestFoodService restBestFoodService, ReviewsService reviewsService, MemberService2 memberService2) {
        this.restBestFoodService = restBestFoodService;
        this.reviewsService = reviewsService;
        this.memberService2 = memberService2;
    }

    //    @GetMapping("")
//    public String find(Model model) {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        model.addAttribute("name", name);
//
////        return "/search/findRest";
//        return "/search/newList";
//    }
    @GetMapping("/find")
    public String find(@RequestParam("name") String name,
                       @RequestParam(value = "sort", required = false, defaultValue = "latest") String sort,
                       Model model) {

        RestInfoDto restInfoDto = restBestFoodService.getBestFoodList(name);

        if (name.equals("") || restInfoDto == null || restInfoDto.getList().size() == 0) {
            model.addAttribute("errorMessage", "휴게소 이름을 다시 한번 확인해주세요.");

            return "/error/errorPage";
        }


        List<ReviewsEntity> reviews = reviewsService.getReviews(restInfoDto.toEntity(), sort);

        double avg = 0.0;
        if (reviews.size() > 0) {
            // 리뷰 평균 rating 설정
            avg = reviewsService.getAvg(reviews.stream().map(ReviewsEntity::toDto).collect(Collectors.toList()));
        }

        UserEntity user = memberService2.getUser();

        List<String> photos = reviewsService.getPhotos(reviews);
        boolean hasImages = photos.size() > 0;

        model.addAttribute("lists", restInfoDto.getList());
        model.addAttribute("avg", avg);
        model.addAttribute("restName", name);
        model.addAttribute("reviews", reviews);
        model.addAttribute("name", user);
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("photos", photos);
        model.addAttribute("currentSort", sort);

        return "search/restInfo";
    }

//    @PostMapping("")
//    public String listInfo(RestFormDto restFormDto, Model model) {
//
//        try {
//            RestInfoDto restInfoDto = restBestFoodService.getBestFoodList(restFormDto.getRestName1());
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//            UserEntity byName = memberService2.getUserByEmail(principalDetails.getUserEmail());
//
//            RestInfoEntity infoFromDb = restBestFoodService.getInfoFromDb(restFormDto.getRestName1());
//            List<ReviewsDto> reviewsByInfo = reviewsService.getReviewsByInfo(infoFromDb);
//
//            if (restInfoDto.getList().size() == 0) {
//                model.addAttribute("errorMessage", "해당 휴게소 정보를 찾을 수 없습니다.");
//                return "error/errorPage"; // errorPage.html로 이동
//            }
//
//            String name = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            model.addAttribute("reviews", reviewsByInfo);
//            model.addAttribute("lists", restInfoDto.getList());
//            model.addAttribute("restName", restFormDto.getRestName1());
//            model.addAttribute("name", name);
//            model.addAttribute("userE", byName);
//            return "/search/list";
//
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "error/errorPage"; // 예외 발생 시 에러 페이지로 이동
//        }
//    }





}
