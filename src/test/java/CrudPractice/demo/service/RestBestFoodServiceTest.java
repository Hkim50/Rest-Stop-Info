package CrudPractice.demo.service;


import CrudPractice.demo.dto.RestInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class RestBestFoodServiceTest {
    @Autowired RestBestFoodService restBestFoodService;

    @Test
    public void test1() {
        String restName = "경주(부산)휴게소";
        String restName2 = "건천(부산)휴게소";

        RestInfoDto restInfoDto = restBestFoodService.getBestFoodList(restName);
        RestInfoDto restInfo2Dto2 = restBestFoodService.getBestFoodList(restName2);


        System.out.println("---------------");
        System.out.println(restInfo2Dto2);
        System.out.println("---------------");
        System.out.println(restInfoDto);

//        restJson.getList().stream()
//                .forEach(f -> {
//                    System.out.println("Name: "+f.getFoodNm()+" coast: "+f.getFoodCost());
//                });


    }
}