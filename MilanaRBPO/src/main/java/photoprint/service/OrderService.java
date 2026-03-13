package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.repository.OrderRepository;
import photoprint.model.entity.Order;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order create(Order order) {
        return repository.save(order);
    }

    public Order get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Order update(Long id, Order order) {
        order.setId(id);
        return repository.save(order);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

