package CrudPractice.demo.repository;

import CrudPractice.demo.domain.TempStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryStoreRepository extends JpaRepository<TempStore, Long> {
    Optional<TempStore> findByTitle(String name);
}
