package CrudPractice.demo.service;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.dto.ApiResponseDto;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.repository.ApiListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ApiSearchService {
    private final RestClient restClient;
    private final ApiListRepository apiListRepository;

    public ApiSearchService(@Qualifier("naverApiClient") RestClient restClient, ApiListRepository repository) {
        this.restClient = restClient;
        this.apiListRepository = repository;
    }


    public ApiResponseDto findStore(String name) {

        ApiResponseDto result = restClient.get()
                .uri("?query=" + name + "&display=5&start=1")
                .retrieve()
                .body(ApiResponseDto.class);

        // filter title name. remove <b> and </b>.
        result.setItems(removeExc(result.getItems()));

        List<ApiListEntity> list = result.getItems().stream()
                .map(ApiListDto::toEntity)
                .toList();

        for (ApiListEntity apiListEntity : list) {
            if (!findByName(apiListEntity.getTitle()).isPresent()) {
                apiListRepository.save(apiListEntity);
            }
        }

        return result;
    }

    public Optional<ApiListEntity> findByName(String name) {
        Optional<ApiListEntity> listEntity = apiListRepository.findByTitle(name);

        return listEntity;
    }

    public List<ApiListEntity> findByName(List<ApiListDto> list) {
//        Optional<ApiListEntity> listEntity = apiListRepository.findByTitle(name);
        List<ApiListEntity> newList = new ArrayList<>();

        list.stream().forEach(f -> {
            Optional<ApiListEntity> byTitle = apiListRepository.findByTitle(f.getTitle());
            newList.add(byTitle.get());
        });

        return newList;
    }

    private List<ApiListDto> removeExc(List<ApiListDto> dto) {

        for (ApiListDto apiListDto : dto) {
            if (apiListDto.getTitle().contains("<b>")) {
                String s = apiListDto.getTitle().replaceAll("<b>", "").replaceAll("</b>", "");
                apiListDto.setTitle(s);
            }
        }
        return dto;
    }


    }
