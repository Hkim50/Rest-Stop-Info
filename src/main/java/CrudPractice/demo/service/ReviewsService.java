package CrudPractice.demo.service;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.dto.ReviewsDto;
import CrudPractice.demo.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewsService{
    private final ReviewsRepository reviewsRepository;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public ReviewsEntity addUserInReview(ReviewsDto reviewsDto, UserEntity user) {
        reviewsDto.setName(user.getName());
        reviewsDto.setUserEntity(user);
        ReviewsEntity entity = reviewsDto.toEntity();
//        ReviewsEntity entity = reviewsDto.toEntity();
//        entity.setUser(user);
        return entity;

    }

    public ReviewsEntity addReview(ReviewsEntity reviewsEntity) {
        return  reviewsRepository.save(reviewsEntity);
    }

    public List<ReviewsDto> getReviewsByInfo(RestInfoEntity restInfoEntity) {
        List<ReviewsEntity> reviewsByRestInfoEntity = reviewsRepository.getReviewByRestInfoEntity(restInfoEntity);

        List<ReviewsDto> list = new ArrayList<>();
        reviewsByRestInfoEntity.stream().forEach(f -> {
            list.add(f.toDto());
        });
        return list;
    }

    public List<ReviewsDto> getReviewByUser(UserEntity user) {
        List<ReviewsEntity> reviewByUserEntity = reviewsRepository.getReviewByUser(user);
        List<ReviewsDto> reviews = new ArrayList<>();

        reviewByUserEntity.stream().forEach(f -> {
            reviews.add(f.toDto());
        });

        return reviews;
    }

    public ReviewsEntity getReviewByNameAndCreatedAt(String name, Timestamp timestamp) {
        ReviewsEntity reviewByNameAndTime = reviewsRepository.getReviewByNameAndCreatedAt(name, timestamp);
        return reviewByNameAndTime;
    }

    public Optional<ReviewsEntity> getReviewById(Long id) {
        Optional<ReviewsEntity> byId = reviewsRepository.findById(id);
        return byId;

    }

    public void removeReview(ReviewsDto reviewsDto) {
        ReviewsEntity reviewByNameAndCreatedAt = getReviewByNameAndCreatedAt(reviewsDto.getName(), reviewsDto.getCreatedAt());
        reviewsRepository.delete(reviewByNameAndCreatedAt);
    }

    public void deleteReview(Long id) {
        reviewsRepository.deleteById(id);
    }

    @Transactional
    public boolean updateReview(ReviewsDto reviewsDto) {
        return getReviewById(reviewsDto.getId())
                .map(review -> {
                    review.updateReview(reviewsDto.getContent(), reviewsDto.getRating());
                    return true;
                })
                .orElse(false);
    }

    public List<ReviewsDto> findByApiList(ApiListDto dto) {
        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.getReviewByApiListEntity(dto.toEntity());

        return reviewByApiListEntity.stream()
                .map(ReviewsEntity::toDto)
                .toList();
    }

    public List<ReviewsDto> findByApiListOrderByCreatedAtDesc(ApiListDto dto) {
        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByCreatedAtDesc(dto.toEntity());

        return reviewByApiListEntity.stream()
                .map(ReviewsEntity::toDto)
                .toList();
    }

    public List<ReviewsDto> findByApiListOrderByRatingDesc(ApiListDto dto) {
        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByRatingDesc(dto.toEntity());

        return reviewByApiListEntity.stream()
                .map(ReviewsEntity::toDto)
                .toList();
    }

    public List<ReviewsDto> findByApiListOrderByRatingAsc(ApiListDto dto) {
        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByRatingAsc(dto.toEntity());

        return reviewByApiListEntity.stream()
                .map(ReviewsEntity::toDto)
                .toList();
    }

    public List<String> getPhotos(List<ReviewsDto> dto) {
        List<String> photos = new ArrayList<>();

        for (ReviewsDto reviewsDto : dto) {
            if (reviewsDto.getFilePath() != null) {
                photos.add(reviewsDto.getFilePath());
            }
        }
        return photos;
    }

    public List<ApiListDto> getProfPhoto(List<ApiListEntity> list) {
        List<ApiListDto> dtoList = new ArrayList<>();

//        list.stream().forEach(f -> {
//            dtoList.add(f.toDto());
//        });

        for (ApiListEntity entity: list) {
            Optional<ReviewsEntity> firstByFilePathIsNotNullAndApiListEntity = reviewsRepository.findFirstByFilePathIsNotNullAndApiListEntity(entity);
            ApiListDto dto = entity.toDto();
            if (firstByFilePathIsNotNullAndApiListEntity.isPresent()) {
                dto.setFilePath(firstByFilePathIsNotNullAndApiListEntity.get().getFilePath());
            }
            else {
                dto.setFilePath("https://placehold.co/400x300");
            }
            dtoList.add(dto);
        }

        return dtoList;
    }





}

