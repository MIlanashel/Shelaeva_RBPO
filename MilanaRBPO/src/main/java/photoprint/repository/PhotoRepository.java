package photoprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photoprint.model.entity.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByFormatIdAndOrdersIsEmpty(Long formatId);

    Optional<Photo> findByFilename(String filename);

}
