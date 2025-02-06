package CrudPractice.demo.controller;

import CrudPractice.demo.dto.RestFormDto;
import CrudPractice.demo.dto.RestJsonDto;
import CrudPractice.demo.service.RestBestFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/find")
@Controller
public class SearchInfoController {
    private final RestBestFoodService restBestFoodService;

    @Autowired
    public SearchInfoController(RestBestFoodService restBestFoodService) {
        this.restBestFoodService = restBestFoodService;
    }

    @GetMapping("")
    public String find() {
        return "/search/findRest";
    }

    @PostMapping("")
    public String listInfo(RestFormDto restFormDto, Model model) {

        try {
            RestJsonDto restJsonDto = restBestFoodService.getBestFoodList(restFormDto.getRestName1());

            if (restJsonDto.getList().size() == 0) {
                model.addAttribute("errorMessage", "해당 휴게소 정보를 찾을 수 없습니다.");
                return "error/errorPage"; // errorPage.html로 이동
            }

            model.addAttribute("lists", restJsonDto.getList());
            model.addAttribute("restName", restFormDto.getRestName1());
            return "/search/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage"; // 예외 발생 시 에러 페이지로 이동
        }
    }





}
