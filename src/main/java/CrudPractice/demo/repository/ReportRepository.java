package CrudPractice.demo.repository;

import CrudPractice.demo.domain.ReportedEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportedEntity, Long> {
    Optional<ReportedEntity> findByReview(ReviewsEntity entity);

    List<ReportedEntity> findTop3ByOrderByCreatedAtDesc();

}
