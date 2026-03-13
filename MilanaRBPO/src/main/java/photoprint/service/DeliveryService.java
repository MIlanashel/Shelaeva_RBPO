package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.repository.DeliveryRepository;
import photoprint.model.entity.Delivery;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public Delivery create(Delivery delivery) {
        return repository.save(delivery);
    }

    public Delivery get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Delivery update(Long id, Delivery delivery) {
        delivery.setId(id);
        return repository.save(delivery);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

