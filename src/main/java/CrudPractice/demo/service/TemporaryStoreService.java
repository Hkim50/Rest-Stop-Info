package CrudPractice.demo.service;

import CrudPractice.demo.dto.ApiListDto;
import CrudPractice.demo.domain.TempStore;
import CrudPractice.demo.repository.TemporaryStoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemporaryStoreService {
    private final TemporaryStoreRepository temporaryStoreRepository;

    public TemporaryStoreService(TemporaryStoreRepository temporaryStoreRepository) {
        this.temporaryStoreRepository = temporaryStoreRepository;
    }

    public Long save(ApiListDto dto) {
        TempStore save = temporaryStoreRepository.save(dto.toStoreForm());

        return save.getId();
    }

    public Optional<TempStore> findByTitle(String title) {
        return temporaryStoreRepository.findByTitle(title);
    }

    public List<TempStore> getAllTempStore() {
        return temporaryStoreRepository.findAll();
    }

    public void delete(Long id) {
        temporaryStoreRepository.deleteById(id);
    }

}
