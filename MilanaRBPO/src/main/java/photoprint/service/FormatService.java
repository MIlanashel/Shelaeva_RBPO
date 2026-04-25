package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.repository.FormatRepository;
import photoprint.model.entity.Format;

@Service
public class FormatService {

    private final FormatRepository repository;

    public FormatService(FormatRepository repository) {
        this.repository = repository;
    }

    public Format create(Format format) {
        repository.findByName(format.getName()).ifPresent(f -> {
            throw new RuntimeException("Format already exists");
        });

        return repository.save(format);
    }

    public Format get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Format update(Long id, Format updated) {
        Format existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Format not found"));

        repository.findByName(updated.getName()).ifPresent(f -> {
            if (!f.getId().equals(id)) {
                throw new RuntimeException("Format with this name already exists");
            }
        });

        existing.setName(updated.getName());
        existing.setPrice(updated.getPrice());

        return repository.save(existing);
    }

    public void delete(Long id) {
        Format format = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Format not found"));

        boolean hasUsedPhotos = format.getPhotos().stream()
                .anyMatch(photo -> !photo.getOrders().isEmpty());

        if (hasUsedPhotos) {
            throw new RuntimeException("Cannot delete format: some photos are used in orders");
        }

        repository.delete(format);
    }
}

