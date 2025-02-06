package CrudPractice.demo.service;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.dto.RestJsonDto;
import CrudPractice.demo.repository.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestBestFoodService {
    private final RestRepository repository;

    @Autowired
    public RestBestFoodService(RestRepository repository) {
        this.repository = repository;
    }

    @Value("${rest-info-db-key}")
    private String KEY;

    public RestJsonDto getBestFoodList(String restName) {

        // If data already exist in database
        if (repository.findByStdRestNm(restName).size() != 0) {
            return getInfoFromDb(restName);
        }

        RestClient restClient = RestClient.builder()
                .baseUrl("https://data.ex.co.kr")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept-Charset", StandardCharsets.UTF_8.name())
                .build();
        // "&bestfoodyn=Y"

        RestJsonDto restJsonDto = restClient.get()
                .uri("https://data.ex.co.kr/openapi/restinfo/restBestfoodList?key=" + KEY + "&type=json&stdRestNm=" + restName)
                .retrieve()
                .body(RestJsonDto.class);

        // Insert into database
        save(restJsonDto);

        return restJsonDto;
    }

    public RestJsonDto getInfoFromDb(String restName) {
        List<RestInfoEntity> list = repository.findByStdRestNm(restName);
        RestJsonDto restJsonDto = new RestJsonDto();

        List<RestInfoDto> list2 = new ArrayList<>();

        for (RestInfoEntity restInfoEntity : list) {
            list2.add(restInfoEntity.toDto());
        }

        restJsonDto.setList(list2);
        System.out.println("Data already exist in DB");
        return restJsonDto;
    }

    public int save(RestJsonDto restJsonDto) {
        restJsonDto.getList().stream()
                .forEach(f -> {
                    repository.save(f.toEntity());
                });
        return restJsonDto.getList().size();
    }
}
