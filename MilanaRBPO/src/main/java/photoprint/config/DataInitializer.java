package photoprint.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import photoprint.model.entity.*;
import photoprint.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final FormatRepository formatRepository;
    private final PhotoRepository photoRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CustomerRepository customerRepository,
                           FormatRepository formatRepository,
                           PhotoRepository photoRepository,
                           OrderRepository orderRepository,
                           DeliveryRepository deliveryRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.formatRepository = formatRepository;
        this.photoRepository = photoRepository;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // 0️⃣ Создание администратора
        userRepository.findByUsername("admin").orElseGet(() -> {
            User admin = new User("admin", passwordEncoder.encode("admin123!"), Role.ADMIN);
            return userRepository.save(admin);
        });

        // 1️⃣ Клиенты
        Customer c1 = customerRepository.findByEmail("ivan@mail.ru")
                .orElseGet(() -> customerRepository.save(
                        new Customer("Иван Иванов", "ivan@mail.ru")));

        Customer c2 = customerRepository.findByEmail("maria@mail.ru")
                .orElseGet(() -> customerRepository.save(
                        new Customer("Мария Петрова", "maria@mail.ru")));

        // 2️⃣ Форматы
        Format f1 = formatRepository.findByName("10x15")
                .orElseGet(() -> formatRepository.save(new Format("10x15", 5.0)));

        Format f2 = formatRepository.findByName("15x20")
                .orElseGet(() -> formatRepository.save(new Format("15x20", 8.0)));

        Format f3 = formatRepository.findByName("20x30")
                .orElseGet(() -> formatRepository.save(new Format("20x30", 12.0)));

        // 3️⃣ Фотографии
        Photo p1 = photoRepository.findByFilename("photo1.jpg")
                .orElseGet(() -> photoRepository.save(new Photo("photo1.jpg", f1)));

        Photo p2 = photoRepository.findByFilename("photo2.jpg")
                .orElseGet(() -> photoRepository.save(new Photo("photo2.jpg", f2)));

        Photo p3 = photoRepository.findByFilename("photo3.jpg")
                .orElseGet(() -> photoRepository.save(new Photo("photo3.jpg", f3)));

        Photo p4 = photoRepository.findByFilename("photo4.jpg")
                .orElseGet(() -> photoRepository.save(new Photo("photo4.jpg", f1)));

        Photo p5 = photoRepository.findByFilename("photo5.jpg")
                .orElseGet(() -> photoRepository.save(new Photo("photo5.jpg", f2)));

        // 4️⃣ Заказы
        if (orderRepository.findByCustomerId(c1.getId()).isEmpty()) {
            Order o1 = new Order();
            o1.setCustomer(c1);
            o1.setPhotos(List.of(p1, p2));
            o1.setPaid(true);
            orderRepository.save(o1);

            Delivery d1 = new Delivery("ул. Ленина, 10", o1);
            o1.setDelivery(d1);
            deliveryRepository.save(d1);
        }

        if (orderRepository.findByCustomerId(c2.getId()).isEmpty()) {
            Order o2 = new Order();
            o2.setCustomer(c2);
            o2.setPhotos(List.of(p3));
            o2.setPaid(false);
            orderRepository.save(o2);
        }

        System.out.println("Тестовые данные проверены / инициализированы, админ создан");
    }
}
