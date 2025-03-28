package CrudPractice.demo.repository;

import CrudPractice.demo.domain.StoreForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryStoreRepository extends JpaRepository<StoreForm, Long> {
    Optional<StoreForm> findByTitle(String name);
}
