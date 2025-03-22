package CrudPractice.demo.domain;

import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.dto.RestListDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="rest_info")
public class RestInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("list")
    @JsonIgnore
    @OneToMany(mappedBy = "restInfoEntity")
    private List<RestListEntity> list;

    @JsonProperty("count")
    private int count;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("numOfRows")
    private int numOfRows;

    @JsonProperty("pageSize")
    private int pageSize;

    @JsonProperty("message")
    private String message;

    @JsonProperty("code")
    private String code;

    public void add() {
        list.stream().forEach(f -> {
            f.setRestInfoEntity(this);
        });
    }

    public RestInfoDto toDto() {
        List<RestListDto> Rlist = new ArrayList<>();
        list.stream().forEach(f ->{
            Rlist.add(f.toDto());
        });

        return RestInfoDto.builder()
                .id(this.id)
                .list(Rlist)
                .count(this.count)
                .pageNo(this.pageNo)
                .numOfRows(this.numOfRows)
                .pageSize(this.pageSize)
                .message(this.message)
                .code(this.code)
                .build();
    }
}
