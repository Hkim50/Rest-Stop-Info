package CrudPractice.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestJson {
    @JsonProperty("list")
    private List<RestInfoDto> list;

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

}
