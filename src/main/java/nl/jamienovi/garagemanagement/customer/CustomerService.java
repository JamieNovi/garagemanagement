package nl.jamienovi.garagemanagement.customer;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.utils.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private CustomerMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomer(int customerId) {
       Optional<Customer> customerOptional =   customerRepository.findById(customerId);
       Customer customer = customerOptional.get();
        log.info(customer.getLastName());
       return customer;
    }

    public void saveCustomer(Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findCustomerByEmail(customer.getEmail());
        if(customerOptional.isPresent()) {
            throw new IllegalStateException("Klant bestaat al in het systeem!");
        }
        customerRepository.save(customer);
    }

    public void updateCustomer(CustomerDto customerDto) {
               Customer existingCustomer = customerRepository.findById(customerDto.getId())
               .orElseThrow(() -> new IllegalStateException(
                       "Klant met id " + customerDto.getId() + " bestaat niet."
               ));
               mapper.updateCustomerFromDto(customerDto,existingCustomer);
               customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(int customerId) {
        boolean exists = customerRepository.existsById(customerId);
        if(!exists) {
            throw new IllegalStateException(String.format("Klant met id %s bestaat niet!",customerId));
        }
        customerRepository.deleteById(customerId);
    }
}
