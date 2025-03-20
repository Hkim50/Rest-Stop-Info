package CrudPractice.demo.repository;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.ApiListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    List<ReviewsEntity> getReviewByRestInfoEntity(RestInfoEntity restInfoEntity);
    List<ReviewsEntity> getReviewByUser(UserEntity user);

    ReviewsEntity getReviewByNameAndCreatedAt(String name, Timestamp timestamp);

    List<ReviewsEntity> getReviewByApiListEntity(ApiListEntity apiListEntity);

    Optional<ReviewsEntity> findFirstByFilePathIsNotNullAndApiListEntity(ApiListEntity entity);

//    Optional<ReviewsEntity> getReviewByApiListEntityIfFilePathIsNotNull(ApiListEntity apiListEntity);


}
