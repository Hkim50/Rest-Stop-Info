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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public ReviewsDto addImage(MultipartFile image, ReviewsDto reviewsDto) throws IOException {
        if (image != null && !image.isEmpty()) {
            String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + image.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);
            image.transferTo(saveFile);

            reviewsDto.setFileName(fileName);
            reviewsDto.setFilePath("/files/" + fileName);
        }
        return reviewsDto;
    }

    public List<ReviewsDto> getReviews(RestInfoEntity restInfoEntity, String sort) {

        List<ReviewsEntity> reviewsByRestInfoEntity;
        if (sort.equals("latest")) {
            reviewsByRestInfoEntity = reviewsRepository.findAllByRestInfoEntityOrderByCreatedAtDesc(restInfoEntity);
        } else if (sort.equals("highRating")) {
            reviewsByRestInfoEntity = reviewsRepository.findAllByRestInfoEntityOrderByRatingDesc(restInfoEntity);
        } else{
            reviewsByRestInfoEntity = reviewsRepository.findAllByRestInfoEntityOrderByRatingAsc(restInfoEntity);
        }

        return reviewsByRestInfoEntity.stream().map(ReviewsEntity::toDto)
                .toList();
    }




    public List<ReviewsDto> getReviewByUser(UserEntity user) {
        List<ReviewsEntity> reviewByUserEntity = reviewsRepository.getReviewByUser(user);
        List<ReviewsDto> reviews = new ArrayList<>();

        reviewByUserEntity.stream().forEach(f -> {
            reviews.add(f.toDto());
        });

        return reviews;
    }

    public double getAvg(List<ReviewsDto> reviews) {
        double avg = 0.0;
        for (ReviewsDto review : reviews) {
            avg += review.getRating();
        }
        avg = avg / reviews.size();
        avg = Math.round(avg * 100.0) / 100.0;
        return avg;
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

    public List<ReviewsEntity> findByApiList(List<ApiListEntity> apiList) {
//        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.getReviewByApiListEntity(dto.toEntity());
//
//        return reviewByApiListEntity.stream()
//                .map(ReviewsEntity::toDto)
//                .toList();
        return reviewsRepository.findAllByApiListEntities(apiList);
    }

    public List<ReviewsDto> findByApiList(ApiListDto dto) {
        List<ReviewsEntity> reviewByApiListEntity = reviewsRepository.getReviewByApiListEntity(dto.toEntity());

        return reviewByApiListEntity.stream()
                .map(ReviewsEntity::toDto)
                .toList();
    }

    public List<ReviewsDto> findByApiList(ApiListDto dto, String sort) {

        List<ReviewsEntity> reviewByApiListEntity;
        if (sort.equals("latest")) {
            reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByCreatedAtDesc(dto.toEntity());
        } else if (sort.equals("highRating")) {
            reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByRatingDesc(dto.toEntity());
        } else{
            reviewByApiListEntity = reviewsRepository.findAllByApiListEntityOrderByRatingAsc(dto.toEntity());
        }

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
                dto.setTopReview(firstByFilePathIsNotNullAndApiListEntity.get().getContent());
            }
            else {
                dto.setFilePath("https://placehold.co/400x300");
            }
            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<ApiListDto> getProfPhoto(List<ApiListEntity> list, List<ReviewsEntity> allReviews) {
        List<ApiListDto> dtoList = new ArrayList<>();

        // filePath가 존재하는 리뷰만 필터링
        Map<ApiListEntity, ReviewsEntity> reviewMap = allReviews.stream()
                .filter(r -> r.getFilePath() != null)
                // getapilistentity 를 해서 얻은 값을 key, 현재 allreview.stream 값을 value, 이미 존재하는 key 가 있다면 원래 있던 값으로 유지
                .collect(Collectors.toMap(ReviewsEntity::getApiListEntity, Function.identity(), (existing, replacement) -> existing));

        // DTO 변환
        for (ApiListEntity entity : list) {
            ApiListDto dto = entity.toDto();

            ReviewsEntity review = reviewMap.get(entity);
            if (review != null) {
                dto.setFilePath(review.getFilePath());
                dto.setTopReview(review.getContent());
            } else {
                dto.setFilePath("https://placehold.co/400x300");
            }

            dtoList.add(dto);
        }

        return dtoList;
    }




}

