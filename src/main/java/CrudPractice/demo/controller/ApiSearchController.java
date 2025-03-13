package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.dto.ApiResponseDto;
import CrudPractice.demo.dto.StoreFormDto;
import CrudPractice.demo.service.ApiSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class ApiSearchController {

    private final ApiSearchService apiSearchService;

    @Autowired
    public ApiSearchController(ApiSearchService apiSearchService) {
        this.apiSearchService = apiSearchService;
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
        model.addAttribute("restaurant", dto);
        return "search/storeInfo";
    }
}
