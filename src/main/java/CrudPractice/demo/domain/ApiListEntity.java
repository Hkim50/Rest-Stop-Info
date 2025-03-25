package CrudPractice.demo.domain;

import CrudPractice.demo.dto.ApiListDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="api_list")
public class ApiListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Column
    private String link;

    @Column
    private String category;

    @Column
    private String description;

    @Column
    private String telephone;

    @Column
    private String address;

    @Column
    private String roadAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiListEntity)) return false;
        ApiListEntity that = (ApiListEntity) o;
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public ApiListDto toDto() {
        return ApiListDto.builder()
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


