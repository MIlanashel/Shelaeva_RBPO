package photoprint.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_seq")
    @SequenceGenerator(name = "photo_seq", sequenceName = "photo_seq", allocationSize = 50)
    private Long id;

    @Column(unique = true, nullable = false)
    private String filename;

    @ManyToOne
    @JoinColumn(name = "format_id", nullable = false)
    @JsonIgnore
    private Format format;

    @ManyToMany(mappedBy = "photos")
    @JsonIgnore
    private List<Order> orders;

    public Photo(String filename, Format format){
        this.format = format;
        this.filename = filename;
    }

    public Photo(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

