package CrudPractice.demo.dto;

import CrudPractice.demo.domain.ApiListEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiListDto {
    private Long id;
    @JsonProperty("title")
    private String title;

    @JsonProperty("link")
    private String link;

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("telephone")
    private String telephone;

    @JsonProperty("address")
    private String address;

    @JsonProperty("roadAddress")
    private String roadAddress;

    @JsonProperty("mapx")
    private String mapx;

    @JsonProperty("mapy")
    private String mapy;

    private String filePath;

    private int numOfReviews;

    private String topDesc;

    public ApiListEntity toEntity() {
        return ApiListEntity.builder()
                .id(this.id)
                .title(this.title)
                .link(this.link)
                .category(this.category)
                .description(this.description)
                .telephone(this.telephone)
                .address(this.address)
                .roadAddress(this.roadAddress)
                .build();
    }

}

