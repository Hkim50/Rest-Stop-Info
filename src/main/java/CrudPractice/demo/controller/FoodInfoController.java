package CrudPractice.demo.controller;

import CrudPractice.demo.dto.RestJson;
import CrudPractice.demo.service.RestBestFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FoodInfoController {
    private final RestBestFoodService restBestFoodService;

    @Autowired
    public FoodInfoController(RestBestFoodService restBestFoodService) {
        this.restBestFoodService = restBestFoodService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/find")
    public String find() {
        return "/search/findRest";
    }

    @PostMapping("/find")
    public String listInfo(RestForm restForm, Model model) {

        try {
            RestJson restJson = restBestFoodService.getBestFoodList(restForm.getRestName1());

            if (restJson.getList().size() == 0) {
                model.addAttribute("errorMessage", "해당 휴게소 정보를 찾을 수 없습니다.");
                return "error/errorPage"; // errorPage.html로 이동
            }

            model.addAttribute("lists", restJson.getList());
            model.addAttribute("restName", restForm.getRestName1());
            return "/search/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage"; // 예외 발생 시 에러 페이지로 이동
        }
    }

    @GetMapping("/compare")
    public String compare() {
        return "/compare/findRest";
    }

    @PostMapping("/compare")
    public String compare(RestForm restForm, Model model) {


        try {
            RestJson restJson1 = restBestFoodService.getBestFoodList(restForm.getRestName1());
            RestJson restJson2 = restBestFoodService.getBestFoodList(restForm.getRestName2());

            if (restJson1.getList().size() == 0 || restJson2.getList().size() == 0) {
                model.addAttribute("errorMessage", "휴게소 정보를 찾을 수 없습니다.");
                return "error/errorPage"; // errorPage.html로 이동
            }

            model.addAttribute("list1", restJson1.getList());
            model.addAttribute("list2", restJson2.getList());

            model.addAttribute("restName1", restForm.getRestName1());
            model.addAttribute("restName2", restForm.getRestName2());

            return "/compare/list";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/errorPage";
        }



    }



}
