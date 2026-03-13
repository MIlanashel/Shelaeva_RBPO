package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.model.entity.Order;
import photoprint.model.entity.Delivery;
import photoprint.model.entity.Photo;
import photoprint.service.OrderBusinessService;

import java.util.List;

@RestController
@RequestMapping("/api/business")
public class OrderBusinessController {

    private final OrderBusinessService service;

    public OrderBusinessController(OrderBusinessService service) {
        this.service = service;
    }

    //Создать заказ с фотографиями

    @PostMapping("/orders/create")
    public Order createOrder(@RequestParam Long customerId, @RequestBody List<Long> photoIds) {
        return service.createOrder(customerId, photoIds);
    }


    //Оплатить заказ
    @PostMapping("/orders/pay/{orderId}")
    public Order payOrder(@PathVariable Long orderId) {
        return service.payOrder(orderId);
    }

    //Создать доставку для оплаченного заказа
    @PostMapping("/orders/delivery/{orderId}")
    public Delivery createDelivery(@PathVariable Long orderId, @RequestParam String address) {
        return service.createDelivery(orderId, address);
    }

    //Найти все заказы клиента
    @GetMapping("/orders/customer/{customerId}")
    public List<Order> getOrdersByCustomer(@PathVariable Long customerId) {
        return service.getOrdersByCustomer(customerId);
    }

    //Список фотографий определённого формата, ещё не заказанных
    @GetMapping("/photos/format/{formatId}")
    public List<Photo> getAvailablePhotos(@PathVariable Long formatId) {
        return service.getAvailablePhotosByFormat(formatId);
    }
}
