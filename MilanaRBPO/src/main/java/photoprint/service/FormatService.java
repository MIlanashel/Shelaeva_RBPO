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
        return repository.save(format);
    }

    public Format get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Format update(Long id, Format format) {
        format.setId(id);
        return repository.save(format);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

