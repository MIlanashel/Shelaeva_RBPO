package photoprint.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import photoprint.model.entity.Customer;
import photoprint.model.entity.Order;
import photoprint.model.entity.Photo;
import photoprint.repository.CustomerRepository;
import photoprint.repository.OrderRepository;
import photoprint.repository.PhotoRepository;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PhotoRepository photoRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        PhotoRepository photoRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.photoRepository = photoRepository;
    }

    @Transactional
    public Order create(Long customerId, List<Long> photoIds) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);

        if (photoIds != null && !photoIds.isEmpty()) {
            List<Photo> photos = photoRepository.findAllById(photoIds);
            order.setPhotos(photos);
        }

        return orderRepository.save(order);
    }

    public Order get(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public Order update(Long id, Boolean paid, List<Long> photoIds) {

        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (paid != null) {
            existing.setPaid(paid);
        }

        if (photoIds != null) {
            List<Photo> photos = photoRepository.findAllById(photoIds);
            existing.setPhotos(photos);
        }

        return orderRepository.save(existing);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
