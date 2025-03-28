package CrudPractice.demo.service;

import CrudPractice.demo.domain.ApiListEntity;
import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.domain.StoreForm;
import CrudPractice.demo.repository.TemporaryStoreRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemporaryStoreService {
    private final TemporaryStoreRepository temporaryStoreRepository;

    public TemporaryStoreService(TemporaryStoreRepository temporaryStoreRepository) {
        this.temporaryStoreRepository = temporaryStoreRepository;
    }

    public Long save(ApiListDto dto) {
        StoreForm save = temporaryStoreRepository.save(dto.toStoreForm());

        return save.getId();
    }

    public Optional<StoreForm> findByTitle(String title) {
        return temporaryStoreRepository.findByTitle(title);
    }

}
