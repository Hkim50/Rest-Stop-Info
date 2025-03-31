package CrudPractice.demo.dto;

import CrudPractice.demo.domain.RestListEntity;
import CrudPractice.demo.domain.RestInfoEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestInfoDto {
    private Long id;

    @JsonProperty("list")
    private List<RestListDto> list;

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


    public RestInfoEntity toEntity() {
        List<RestListEntity> Rlist = new ArrayList<>();

        list.stream().forEach(f -> {
            Rlist.add(f.toEntity());
        });

        return RestInfoEntity.builder()
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
