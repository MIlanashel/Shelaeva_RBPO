package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.service.DeliveryService;
import photoprint.model.entity.Delivery;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Delivery create(@RequestParam Long orderId,
                           @RequestParam String address) {
        return service.create(orderId, address);
    }

    // GET
    @GetMapping("/{id}")
    public Delivery get(@PathVariable Long id) {
        return service.get(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Delivery update(@PathVariable Long id,
                           @RequestParam String address) {
        return service.update(id, address);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

