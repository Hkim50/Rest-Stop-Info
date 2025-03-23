package CrudPractice.demo.controller;

import CrudPractice.demo.domain.ReportedEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.dto.ErrorResponse;
import CrudPractice.demo.dto.ReviewReportRequest;
import CrudPractice.demo.repository.ReportRepository;
import CrudPractice.demo.repository.ReviewsRepository;
import CrudPractice.demo.service.ReviewReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReviewReportService reportService;

    public ReportController(ReviewReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity reportReview(@RequestBody ReviewReportRequest request) {
        return reportService.reportReview(request);
    }

    @DeleteMapping("/reject/{reportId}")
    public ResponseEntity rejectReport(@PathVariable("reportId") Long id) {
        reportService.reportReject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity approvedReport(@PathVariable("reviewId") long id, @RequestParam(name = "reportId") Long reportId) {
        reportService.reportApproved(id, reportId);
        return ResponseEntity.ok().build();
    }

}
