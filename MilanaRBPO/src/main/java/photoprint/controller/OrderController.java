package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.model.entity.Order;
import photoprint.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Order create(@RequestParam Long customerId,
                        @RequestParam(required = false) List<Long> photoIds) {
        return service.create(customerId, photoIds);
    }

    // GET
    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.get(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Order update(@PathVariable Long id,
                        @RequestParam(required = false) Boolean paid,
                        @RequestParam(required = false) List<Long> photoIds) {
        return service.update(id, paid, photoIds);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
