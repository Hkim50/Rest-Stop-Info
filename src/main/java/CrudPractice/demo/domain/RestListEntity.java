package CrudPractice.demo.domain;

import CrudPractice.demo.dto.RestListDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="rest_list")
public class RestListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restInfoEntity_id")
    private RestInfoEntity restInfoEntity;

    @JsonProperty("pageNo")
    private Integer pageNo;

    @JsonProperty("numOfRows")
    private Integer numOfRows;

    @JsonProperty("stdRestCd")
    private String stdRestCd;

    @JsonProperty("stdRestNm")
    private String stdRestNm;

    @JsonProperty("lsttmAltrUser")
    private String lsttmAltrUser;

    @JsonProperty("lsttmAltrDttm")
    private String lsttmAltrDttm;

    @JsonProperty("svarAddr")
    private String svarAddr;

    @JsonProperty("routeCd")
    private String routeCd;

    @JsonProperty("routeNm")
    private String routeNm;

    @JsonProperty("seq")
    private String seq;

    @JsonProperty("foodNm")
    private String foodNm;

    @JsonProperty("foodCost")
    private String foodCost;

    @JsonProperty("etc")
    private String etc;

    @JsonProperty("recommendyn")
    private String recommendyn;

    @JsonProperty("seasonMenu")
    private String seasonMenu;

    @JsonProperty("bestfoodyn")
    private String bestfoodyn;

    @JsonProperty("premiumyn")
    private String premiumyn;

    @JsonProperty("app")
    private String app;

    @JsonProperty("restCd")
    private String restCd;

    @JsonProperty("foodMaterial")
    private String foodMaterial;

    @JsonProperty("lastId")
    private String lastId;

    @JsonProperty("lastDtime")
    private String lastDtime;

    public void setRestInfoEntity(RestInfoEntity restInfoEntity) {
        this.restInfoEntity = restInfoEntity;
    }

    public RestListDto toDto() {
        return RestListDto.builder()
                .pageNo(this.pageNo)
                .numOfRows(this.numOfRows)
                .stdRestCd(this.stdRestCd)
                .stdRestNm(this.stdRestNm)
                .lsttmAltrUser(this.lsttmAltrUser)
                .lsttmAltrDttm(this.lsttmAltrDttm)
                .svarAddr(this.svarAddr)
                .routeCd(this.routeCd)
                .routeNm(this.routeNm)
                .seq(this.seq)
                .foodNm(this.foodNm)
                .foodCost(this.foodCost)
                .etc(this.etc)
                .recommendyn(this.recommendyn)
                .seasonMenu(this.seasonMenu)
                .bestfoodyn(this.bestfoodyn)
                .premiumyn(this.premiumyn)
                .app(this.app)
                .restCd(this.restCd)
                .foodMaterial(this.foodMaterial)
                .lastId(this.lastId)
                .lastDtime(this.lastDtime)
                .build();
    }
}
