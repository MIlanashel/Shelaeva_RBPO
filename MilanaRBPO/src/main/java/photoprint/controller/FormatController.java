package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.service.FormatService;
import photoprint.model.entity.Format;

@RestController
@RequestMapping("/formats")
public class FormatController {

    private final FormatService service;

    public FormatController(FormatService service) {
        this.service = service;
    }

    @PostMapping
    public Format create(@RequestBody Format format) {
        return service.create(format);
    }

    @GetMapping("/{id}")
    public Format get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Format update(@PathVariable Long id, @RequestBody Format format) {
        return service.update(id, format);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

