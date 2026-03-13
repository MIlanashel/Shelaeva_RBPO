package photoprint.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
//import photoprint.model.*;
import photoprint.model.entity.Customer;
import photoprint.model.entity.Delivery;
import photoprint.model.entity.Order;
import photoprint.model.entity.Photo;
import photoprint.repository.*;

import java.util.List;

@Service
public class OrderBusinessService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PhotoRepository photoRepository;
    private final DeliveryRepository deliveryRepository;

    public OrderBusinessService(OrderRepository orderRepository,
                                CustomerRepository customerRepository,
                                PhotoRepository photoRepository,
                                DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.photoRepository = photoRepository;
        this.deliveryRepository = deliveryRepository;
    }

    // Создать заказ с фотографиями
    @Transactional
    public Order createOrder(Long customerId, List<Long> photoIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));

        List<Photo> photos = photoRepository.findAllById(photoIds);
        if (photos.isEmpty()) {
            throw new IllegalArgumentException("Заказ не может быть пустым");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setPhotos(photos);
        order.setPaid(false);

        return orderRepository.save(order);
    }

    // Оплатить заказ
    @Transactional
    public Order payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (order.getPhotos() == null || order.getPhotos().isEmpty()) {
            throw new IllegalStateException("Нельзя оплатить пустой заказ");
        }

        order.setPaid(true);
        return orderRepository.save(order);
    }

    // Создать доставку для оплаченного заказа
    @Transactional
    public Delivery createDelivery(Long orderId, String address) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        if (!Boolean.TRUE.equals(order.getPaid())) {
            throw new IllegalStateException("Доставка возможна только для оплаченного заказа");
        }

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(address);

        order.setDelivery(delivery);

        deliveryRepository.save(delivery);
        orderRepository.save(order);

        return delivery;
    }

    // Найти все заказы клиента
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Список фотографий определённого формата, ещё не заказанных
    public List<Photo> getAvailablePhotosByFormat(Long formatId) {
        return photoRepository.findAllByFormatIdAndOrdersIsEmpty(formatId);
    }
}
