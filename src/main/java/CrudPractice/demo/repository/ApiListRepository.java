package CrudPractice.demo.repository;

import CrudPractice.demo.domain.ApiListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApiListRepository extends JpaRepository<ApiListEntity, Long> {
    Optional<ApiListEntity> findByTitle(String name);

    @Query("select a from ApiListEntity a where a.title IN :titles")
    List<ApiListEntity> findByTitles(@Param("titles") List<String> titles);
}
