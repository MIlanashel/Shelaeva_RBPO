package photoprint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import photoprint.model.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
