package CrudPractice.demo.service;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.domain.RestListEntity;
import CrudPractice.demo.dto.RestListDto;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.repository.RestJsonRepository;
import CrudPractice.demo.repository.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestBestFoodService {
    private final RestRepository repository;
    private final RestJsonRepository restJsonRepository;

    @Autowired
    public RestBestFoodService(RestRepository repository, RestJsonRepository restJsonRepository) {
        this.repository = repository;
        this.restJsonRepository = restJsonRepository;
    }

    @Value("${rest-info-db-key}")
    private String KEY;

    public RestInfoDto getBestFoodList(String restName) {

        // If data already exist in database
        if (repository.findByStdRestNm(restName).isPresent()) {
            return getInfoFromDb(restName).toDto();
        }

        RestClient restClient = RestClient.builder()
                .baseUrl("https://data.ex.co.kr")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept-Charset", StandardCharsets.UTF_8.name())
                .build();
        // "&bestfoodyn=Y"

        RestInfoDto restInfoDto = restClient.get()
                .uri("/openapi/restinfo/restBestfoodList?key=" + KEY + "&type=json&stdRestNm=" + restName)
                .retrieve()
                .body(RestInfoDto.class);

        // Insert into database
        save(restInfoDto);

        return restInfoDto;
    }

    public RestInfoEntity getInfoFromDb(String restName) {
        List<RestListEntity> list = repository.findByStdRestNm(restName).get();
        RestListEntity restListEntity1 = list.stream().findAny().get();
        RestInfoEntity restInfoEntity = restListEntity1.getRestInfoEntity();

        return restInfoEntity;

//        RestInfoDto restInfoDto = new RestInfoDto();
//
//        List<RestListDto> list2 = new ArrayList<>();
//
//        for (RestListEntity restListEntity : list) {
//            list2.add(restListEntity.toDto());
//        }
//
//        restInfoDto.setList(list2);
//        System.out.println("Data already exist in DB");
//        return restInfoDto;
    }

    public int save(RestInfoDto restInfoDto) {
        RestInfoEntity savedEntity = restJsonRepository.save(restInfoDto.toEntity());

        // set fk for list
        savedEntity.add();

        savedEntity.getList().stream().forEach(f ->{
            repository.save(f);
        });

        return restInfoDto.getList().size();
    }
}
