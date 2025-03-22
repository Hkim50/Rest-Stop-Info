package CrudPractice.demo.repository;

import CrudPractice.demo.domain.RestListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestRepository extends JpaRepository<RestListEntity, Long> {
    Optional<List<RestListEntity>> findByStdRestNm(String stdRestNm);
}
