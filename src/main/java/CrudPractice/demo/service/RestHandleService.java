package CrudPractice.demo.service;

import CrudPractice.demo.domain.RestListEntity;
import CrudPractice.demo.dto.RestListDto;
import CrudPractice.demo.dto.RestInfoDto;
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

    public RestInfoDto findAll() {
        List<RestListEntity> list = repository.findAll();
        RestInfoDto restInfoDto = new RestInfoDto();
        List<RestListDto> list2 = new ArrayList<>();

        for (RestListEntity restListEntity : list) {
            list2.add(restListEntity.toDto());
        }

        restInfoDto.setList(list2);
        return restInfoDto;
    }

}
