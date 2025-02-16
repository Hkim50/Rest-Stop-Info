package CrudPractice.demo.repository;

import CrudPractice.demo.domain.RestInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestJsonRepository extends JpaRepository<RestInfoEntity, Long> {

}
