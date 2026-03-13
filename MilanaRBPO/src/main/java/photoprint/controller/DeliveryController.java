package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.service.DeliveryService;
import photoprint.model.entity.Delivery;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    @PostMapping
    public Delivery create(@RequestBody Delivery delivery) {
        return service.create(delivery);
    }

    @GetMapping("/{id}")
    public Delivery get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id, @RequestBody Delivery delivery) {
        return service.update(id, delivery);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

