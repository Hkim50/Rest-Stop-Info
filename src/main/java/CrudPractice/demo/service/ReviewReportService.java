package CrudPractice.demo.service;

import CrudPractice.demo.domain.ReportedEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.dto.ErrorResponse;
import CrudPractice.demo.dto.ReviewReportRequest;
import CrudPractice.demo.repository.ReportRepository;
import CrudPractice.demo.repository.ReviewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewReportService {

    private final ReportRepository repository;
    private final ReviewsRepository reviewsRepository;

    public ReviewReportService(ReportRepository repository, ReviewsRepository reviewsRepository) {
        this.repository = repository;
        this.reviewsRepository = reviewsRepository;
    }

    public ResponseEntity reportReview(ReviewReportRequest request) {

        ReviewsEntity reviewsEntity = reviewsRepository.findById(request.getReviewId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        Optional<ReportedEntity> alreadyReported = repository.findByReview(reviewsEntity);
        if (alreadyReported.isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Review already reported"));

        ReportedEntity entity = new ReportedEntity(reviewsEntity, request.getReason(), request.getReporter());
        ReportedEntity savedReport = repository.save(entity);

        return ResponseEntity.ok(savedReport);
    }

    public List<ReportedEntity> getReportedList() {
        return repository.findAll();
    }

    public List<ReportedEntity> getRecentReportedList() {
        return repository.findByOrderByCreatedAtDesc();
    }

    public Long getTotal() {
        return repository.count();
    }

    public void reportReject(Long id) {
        repository.deleteById(id);
    }

    public void reportApproved(Long reviewId, Long reportId) {
        repository.deleteById(reportId);
        reviewsRepository.deleteById(reviewId);
    }
}
