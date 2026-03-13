package photoprint.service;

import org.springframework.stereotype.Service;
import photoprint.repository.CustomerRepository;
import photoprint.model.entity.Customer;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    public Customer get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public Customer update(Long id, Customer customer) {
        customer.setId(id);
        return repository.save(customer);
    }

    public void delete(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Удаляем все заказы клиента
        customer.getOrders().forEach(order -> order.setCustomer(null));

        repository.delete(customer);
    }
}

