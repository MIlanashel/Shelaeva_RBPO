package photoprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photoprint.model.entity.Format;

import java.util.Optional;

public interface FormatRepository extends JpaRepository<Format, Long> {

    Optional<Format> findByName(String name);

}
