package CrudPractice.demo.repository;

import CrudPractice.demo.domain.RestInfoEntity;
import CrudPractice.demo.dto.RestInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestRepository extends JpaRepository<RestInfoEntity, Long> {
    List<RestInfoEntity> findByStdRestNm(String stdRestNm);
}
