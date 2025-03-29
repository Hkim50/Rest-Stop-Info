package CrudPractice.demo.dto;

import CrudPractice.demo.domain.ReviewsEntity;
import CrudPractice.demo.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsDto {

    private Long id;
    private String content;
    private Timestamp createdAt;
    private String restNm;
    private String name;
    private int rating;
    private String fileName;
    private String filePath;
    private UserEntity userEntity;

    public ReviewsEntity toEntity() {
        return ReviewsEntity.builder()
                .content(this.content)
                .createdAt(this.createdAt)
                .name(this.name)
                .rating(this.rating)
                .user(this.userEntity)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .build();
    }
}
