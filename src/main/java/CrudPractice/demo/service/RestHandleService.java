package CrudPractice.demo.service;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.dto.RestInfoDto;
import CrudPractice.demo.dto.RestJsonDto;
import CrudPractice.demo.repository.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestHandleService {
    private final RestRepository repository;

    @Autowired
    public RestHandleService(RestRepository repository) {
        this.repository = repository;
    }

    public RestJsonDto findAll() {
        List<RestInfoEntity> list = repository.findAll();
        RestJsonDto restJsonDto = new RestJsonDto();
        List<RestInfoDto> list2 = new ArrayList<>();

        for (RestInfoEntity restInfoEntity : list) {
            list2.add(restInfoEntity.toDto());
        }

        restJsonDto.setList(list2);
        return restJsonDto;
    }

}
