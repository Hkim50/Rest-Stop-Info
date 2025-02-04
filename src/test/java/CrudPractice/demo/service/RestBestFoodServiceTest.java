package CrudPractice.demo.service;


import CrudPractice.demo.dto.RestJson;
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
        RestJson restJson = restBestFoodService.getBestFoodList(restName);


        restJson.getList().stream()
                .forEach(f -> {
                    System.out.println("Name: "+f.getFoodNm()+" coast: "+f.getFoodCost());
                });


    }
}