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

    @GetMapping("/find")
    public String find() {
        return "findRest";
    }

    @PostMapping("/find")
    public String listInfo(RestForm restForm, Model model) {
        RestJson restJson = restBestFoodService.getBestFoodList(restForm.getRestName());

        model.addAttribute("lists", restJson.getList());
        model.addAttribute("restName", restForm.getRestName());
        return "list";
    }
}
