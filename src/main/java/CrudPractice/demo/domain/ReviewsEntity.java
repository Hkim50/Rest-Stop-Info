package CrudPractice.demo.domain;

import CrudPractice.demo.dto.ReviewsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Table(name="reviews")
public class ReviewsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    private Timestamp createdAt;

    private String name;

    private int rating;

    private String fileName;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "restInfoEntity_id")
    private RestInfoEntity restInfoEntity;

    @ManyToOne
    @JoinColumn(name = "apiListEntity_id")
    private ApiListEntity apiListEntity;

    public ReviewsDto toDto() {
        return ReviewsDto.builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .name(this.name)
                .rating(this.rating)
                .userEntity(this.user)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .build();
    }

    public void updateReview(String content, int rating) {
        this.content = content;
        this.rating = rating;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
    @PrePersist
    @PreUpdate
    private void validate() {
        if (restInfoEntity == null && apiListEntity == null) {
            throw new IllegalStateException("restInfoEntity와 apiListEntity 중 하나는 반드시 존재해야 합니다.");
        }
    }

    public void setApiListEntity(ApiListEntity apiListEntity) {
        this.apiListEntity = apiListEntity;
    }

    public void setRestInfoEntity(RestInfoEntity restInfoEntity) {
        this.restInfoEntity = restInfoEntity;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
