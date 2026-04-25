package photoprint.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import photoprint.model.entity.Delivery;
import photoprint.model.entity.Order;
import photoprint.repository.DeliveryRepository;
import photoprint.repository.OrderRepository;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;
    private final OrderRepository orderRepository;

    public DeliveryService(DeliveryRepository repository,
                           OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Delivery create(Long orderId, String address) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setOrder(order);

        order.setDelivery(delivery);

        return repository.save(delivery);
    }

    public Delivery get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    @Transactional
    public Delivery update(Long id, String address) {
        Delivery existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        existing.setAddress(address);
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
