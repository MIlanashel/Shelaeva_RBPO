package photoprint.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "order_photos",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;

    @Column(nullable = false)
    private Boolean paid = false;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Delivery delivery;

    public Order() {}

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        if (delivery != null) {
            delivery.setOrder(this);
        }
    }

    public Long getId() { return id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }

    public Boolean getPaid() { return paid; }
    public void setPaid(Boolean paid) { this.paid = paid; }

    public Delivery getDelivery() { return delivery; }
}
