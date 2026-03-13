package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.repository.PhotoRepository;
import photoprint.model.entity.Photo;

@Service
public class PhotoService {

    private final PhotoRepository repository;

    public PhotoService(PhotoRepository repository) {
        this.repository = repository;
    }

    public Photo create(Photo photo) {
        return repository.save(photo);
    }

    public Photo get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Photo update(Long id, Photo photo) {
        photo.setId(id);
        return repository.save(photo);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
