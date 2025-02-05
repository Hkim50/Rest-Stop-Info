package CrudPractice.demo.service;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.dto.RestJson;
import CrudPractice.demo.repository.RestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestBestFoodService {
    private final RestRepository repository;

    @Autowired
    public RestBestFoodService(RestRepository repository) {
        this.repository = repository;
    }

    @Value("${rest-info-db-key}")
    private String KEY;

    public RestJson getBestFoodList(String restName) {

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

        RestJson restJson = restClient.get()
                .uri("https://data.ex.co.kr/openapi/restinfo/restBestfoodList?key=" + KEY + "&type=json&stdRestNm=" + restName)
                .retrieve()
                .body(RestJson.class);

        // Insert into database
        save(restJson);

        return restJson;
    }

    public RestJson getInfoFromDb(String restName) {
        List<RestInfoEntity> list = repository.findByStdRestNm(restName);
        RestJson restJson = new RestJson();

        List<RestInfoDto> list2 = new ArrayList<>();

        for (RestInfoEntity restInfoEntity : list) {
            list2.add(restInfoEntity.toDto());
        }

        restJson.setList(list2);
        System.out.println("Data already exist in DB");
        return restJson;
    }

    public int save(RestJson restJson) {
        restJson.getList().stream()
                .forEach(f -> {
                    repository.save(f.toEntity());
                });
        return restJson.getList().size();
    }
}
