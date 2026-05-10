package photoprint.controller;

import org.springframework.web.bind.annotation.*;
import photoprint.service.PhotoService;
import photoprint.model.entity.Photo;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService service;

    public PhotoController(PhotoService service) {
        this.service = service;
    }

    @PostMapping
    public Photo create(@RequestBody Photo photo) {
        return service.create(photo);
    }

    @GetMapping("/{id}")
    public Photo get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Photo update(@PathVariable Long id, @RequestBody Photo photo) {
        return service.update(id, photo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

