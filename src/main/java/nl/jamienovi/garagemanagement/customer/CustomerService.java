package nl.jamienovi.garagemanagement.customer;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class represents business logic for all Customer operations
 *
 * @version 1 10 Sept 2021
 * @author Jamie Spekman
 */
@Slf4j
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DtoMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomer(Integer customerId) {
//        Customer customer = customerRepository.getById(customerId);
//        CustomerGetDto dto = DtoMapper.INSTANCE.customerToDto(customer);
//        log.info(dto.toString());
       return customerRepository.findById(customerId)
               .orElseThrow(() -> new EntityNotFoundException(Customer.class,"id", customerId.toString()));
    }

    public Integer saveCustomer(Customer customer) {
        if(customerRepository.emailAlreadyExists(customer.getEmail())) {
            throw new IllegalStateException("Email bestaat al in het systeem!");
        }
        log.info(String.format("Klant aangemaakt met klant-id:",
                customer.getId()));
       Customer newCustomer = customerRepository.save(customer);
       return newCustomer.getId();
    }

    public void updateCustomer(Integer customerId, CustomerUpdateDto customerUpdateDto) {
               Customer existingCustomer = customerRepository.findById(customerId)
               .orElseThrow(() -> new EntityNotFoundException(
                       Customer.class,"id",
                       customerId.toString())
               );

               mapper.updateCustomerFromDto(customerUpdateDto,existingCustomer);
               customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Customer.class,
                        "id",
                        customerId.toString())
                );
        customerRepository.delete(customer);
    }

    public List<Customer> getCustomerCallingList() {
        return customerRepository.getCallingList();
    }
}
