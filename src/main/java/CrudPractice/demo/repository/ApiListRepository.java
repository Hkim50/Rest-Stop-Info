package CrudPractice.demo.repository;

import CrudPractice.demo.domain.ApiListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiListRepository extends JpaRepository<ApiListEntity, Long> {
    Optional<ApiListEntity> findByName(String name);
}
