package CrudPractice.demo.repository;

import CrudPractice.demo.domain.RestListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestRepository extends JpaRepository<RestListEntity, Long> {
    List<RestListEntity> findByStdRestNm(String stdRestNm);
}
