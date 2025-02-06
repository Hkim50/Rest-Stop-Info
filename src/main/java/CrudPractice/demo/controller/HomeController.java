package CrudPractice.demo.controller;

import CrudPractice.demo.service.RestBestFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final RestBestFoodService restBestFoodService;

    @Autowired
    public HomeController(RestBestFoodService restBestFoodService) {
        this.restBestFoodService = restBestFoodService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
