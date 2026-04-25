package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.model.entity.Format;
import photoprint.repository.FormatRepository;
import photoprint.repository.PhotoRepository;
import photoprint.model.entity.Photo;

@Service
public class PhotoService {

    private final PhotoRepository repository;
    private final FormatRepository formatRepository;


    public PhotoService(PhotoRepository repository, FormatRepository formatRepository) {
        this.repository = repository;
        this.formatRepository = formatRepository;
    }

    public Photo create(Photo photo) {

        repository.findByFilename(photo.getFilename()).ifPresent(p -> {
            throw new RuntimeException("Photo already exists");
        });

        if (photo.getFormat() == null || photo.getFormat().getId() == null) {
            throw new RuntimeException("Format id is required");
        }

        Format format = formatRepository.findById(photo.getFormat().getId())
                .orElseThrow(() -> new RuntimeException("Format not found"));

        photo.setFormat(format);

        return repository.save(photo);
    }

    public Photo get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Photo update(Long id, Photo updated) {
        Photo existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        repository.findByFilename(updated.getFilename()).ifPresent(p -> {
            if (!p.getId().equals(id)) {
                throw new RuntimeException("Photo with this filename already exists");
            }
        });

        existing.setFilename(updated.getFilename());

        return repository.save(existing);
    }

    public void delete(Long id) {
        Photo photo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        if (!photo.getOrders().isEmpty()) {
            throw new RuntimeException("Cannot delete photo: used in orders");
        }

        repository.delete(photo);
    }
}
