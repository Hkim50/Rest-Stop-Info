package CrudPractice.demo.repository;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.ApiListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    List<ReviewsEntity> getReviewByRestInfoEntity(RestInfoEntity restInfoEntity);

    // 리뷰 최신순 (휴게소)
    List<ReviewsEntity> findAllByRestInfoEntityOrderByCreatedAtDesc(RestInfoEntity restInfoEntity);

    // 별점 높은순 (휴게소)
    List<ReviewsEntity> findAllByRestInfoEntityOrderByRatingDesc(RestInfoEntity restInfoEntity);

    // 별점 낮은순 (휴게소)
    List<ReviewsEntity> findAllByRestInfoEntityOrderByRatingAsc(RestInfoEntity restInfoEntity);

    List<ReviewsEntity> getReviewByUser(UserEntity user);

    ReviewsEntity getReviewByNameAndCreatedAt(String name, Timestamp timestamp);

    List<ReviewsEntity> getReviewByApiListEntity(ApiListEntity apiListEntity);

    @Query("SELECT r FROM ReviewsEntity r WHERE r.apiListEntity IN :apiLists")
    List<ReviewsEntity> findAllByApiListEntities(@Param("apiLists") List<ApiListEntity> apiLists);

    // 최신순
    List<ReviewsEntity> findAllByApiListEntityOrderByCreatedAtDesc(ApiListEntity apiListEntity);

    // 별점 높은 순
    List<ReviewsEntity> findAllByApiListEntityOrderByRatingDesc(ApiListEntity apiListEntity);

    // 별점 낮은 순
    List<ReviewsEntity> findAllByApiListEntityOrderByRatingAsc(ApiListEntity apiListEntity);

    Optional<ReviewsEntity> findFirstByFilePathIsNotNullAndApiListEntity(ApiListEntity entity);

//    @Query("SELECT r FROM ReviewsEntity r JOIN FETCH r.apiListEntity WHERE r.apiListEntity.title = :title ORDER BY " +
//            "CASE WHEN :sort = 'latest' THEN r.createdAt END DESC, " +
//            "CASE WHEN :sort = 'highRating' THEN r.rating END DESC, " +
//            "CASE WHEN :sort = 'lowRating' THEN r.rating END ASC")
//    List<ReviewsEntity> findReviewsByTitleWithApi(@Param("title") String title, @Param("sort") String sort);

    @Query("SELECT r FROM ReviewsEntity r JOIN FETCH r.apiListEntity WHERE r.apiListEntity.title = :title ORDER BY r.createdAt DESC")
    List<ReviewsEntity> findReviewsByTitleWithApiLatest(@Param("title") String title);

    @Query("SELECT r FROM ReviewsEntity r JOIN FETCH r.apiListEntity WHERE r.apiListEntity.title = :title ORDER BY r.rating DESC")
    List<ReviewsEntity> findReviewsByTitleWithApiHighRating(@Param("title") String title);

    @Query("SELECT r FROM ReviewsEntity r JOIN FETCH r.apiListEntity WHERE r.apiListEntity.title = :title ORDER BY r.rating ASC")
    List<ReviewsEntity> findReviewsByTitleWithApiLowRating(@Param("title") String title);




//    Optional<ReviewsEntity> getReviewByApiListEntityIfFilePathIsNotNull(ApiListEntity apiListEntity);


}
