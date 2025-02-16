package CrudPractice.demo.repository;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    List<ReviewsEntity> getReviewByRestInfoEntity(RestInfoEntity restInfoEntity);
    List<ReviewsEntity> getReviewByUser(UserEntity user);

    ReviewsEntity getReviewByNameAndCreatedAt(String name, Timestamp timestamp);

}
