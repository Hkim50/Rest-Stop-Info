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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "restInfoEntity_id")
    private RestInfoEntity restInfoEntity;

    public ReviewsDto toDto() {
        return ReviewsDto.builder()
                .content(this.content)
                .createdAt(this.createdAt)
                .name(this.name)
                .rating(this.rating)
                .userEntity(this.user)
                .build();
    }

    public void updateReview(String content, int rating) {
        this.content = content;
        this.rating = rating;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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
