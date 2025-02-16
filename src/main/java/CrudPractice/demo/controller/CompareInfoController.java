package CrudPractice.demo.controller;


import CrudPractice.demo.dto.RestFormDto;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.service.RestBestFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompareInfoController {

    private final RestBestFoodService restBestFoodService;

    @Autowired
    public CompareInfoController(RestBestFoodService restBestFoodService) {
        this.restBestFoodService = restBestFoodService;
    }

    @GetMapping("/compare")
    public String compare(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("name", name);
        return "/compare/findRest";
    }

    @PostMapping("/compare")
    public String compare(RestFormDto restFormDto, Model model) {


        try {
            RestInfoDto restInfoDto1 = restBestFoodService.getBestFoodList(restFormDto.getRestName1());
            RestInfoDto restInfo2Dto = restBestFoodService.getBestFoodList(restFormDto.getRestName2());

            if (restInfoDto1.getList().size() == 0 || restInfo2Dto.getList().size() == 0) {
                model.addAttribute("errorMessage", "휴게소 정보를 찾을 수 없습니다.");
                return "error/errorPage"; // errorPage.html로 이동
            }

            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("name", name);

            model.addAttribute("list1", restInfoDto1.getList());
            model.addAttribute("list2", restInfo2Dto.getList());

            model.addAttribute("restName1", restFormDto.getRestName1());
            model.addAttribute("restName2", restFormDto.getRestName2());

            return "/compare/list";

        } catch (Exception e) {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            model.addAttribute("name", name);
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }



    }
}
