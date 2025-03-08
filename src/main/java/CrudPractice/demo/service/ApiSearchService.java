package CrudPractice.demo.service;

import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.dto.ApiResponseDto;
import CrudPractice.demo.dto.RestInfoDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ApiSearchService {
    private final RestClient restClient;

    public ApiSearchService(@Qualifier("naverApiClient") RestClient restClient) {
        this.restClient = restClient;
    }


    public ApiResponseDto findStore(String name) {

        ApiResponseDto result = restClient.get()
                .uri("?query=" + name + "&display=5&start=1")
                .retrieve()
                .body(ApiResponseDto.class);

        return result;

    }

    public ApiListDto findByName(String name) {

        ApiResponseDto result = restClient.get()
                .uri("?query=" + name + "&display=1&start=1")
                .retrieve()
                .body(ApiResponseDto.class);

        return result.getItems().get(0);
    }


    }
