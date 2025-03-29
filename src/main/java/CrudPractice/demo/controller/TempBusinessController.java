package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.TempStore;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.service.ApiSearchService;
import CrudPractice.demo.service.TemporaryStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/claim")
public class TempBusinessController {

    private final ApiSearchService apiSearchService;
    private final TemporaryStoreService temporaryStoreService;

    public TempBusinessController(ApiSearchService apiSearchService, TemporaryStoreService temporaryStoreService) {
        this.apiSearchService = apiSearchService;
        this.temporaryStoreService = temporaryStoreService;
    }

    @GetMapping
    public String claim() {
        return "claim";
    }

    @GetMapping("/name")
    @ResponseBody
    public ResponseEntity<String> validateBusinessName(@RequestParam("name") String name) {
        // 이미 등록되었는지 검사
        Optional<ApiListEntity> existingBusiness = apiSearchService.findByName(name);
        // 이미 임시 저장소에 등록되었는지 검사
        Optional<TempStore> existingTemp = temporaryStoreService.findByTitle(name);

        if (existingBusiness.isPresent() || existingTemp.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 가게입니다.");

        return ResponseEntity.ok("생성 가능");
    }

    @PostMapping("/temp-save")
    @ResponseBody
    public ResponseEntity<Long> addBusiness(@RequestBody ApiListDto dto) {
        Long save = temporaryStoreService.save(dto);

        return ResponseEntity.ok(save);
    }

    @PostMapping("/approve/{businessId}")
    @ResponseBody
    public ResponseEntity<String> saveBusiness(@PathVariable("businessId") Long id, @RequestBody ApiListDto dto) {
        // api 리스트에 저장
        apiSearchService.save(dto);
        // tempStore 에서 삭제
        temporaryStoreService.delete(id);

        return ResponseEntity.ok("승인 완료");
    }

    @DeleteMapping("/reject/{businessId}")
    @ResponseBody
    public ResponseEntity<String> rejectBusiness(@PathVariable("businessId") Long id) {
        // tempStore 에서 삭제
        temporaryStoreService.delete(id);
        return ResponseEntity.ok("거부 완료");
    }


}
