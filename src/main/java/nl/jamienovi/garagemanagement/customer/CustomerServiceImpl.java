package nl.jamienovi.garagemanagement.customer;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EmailDuplicateException;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.interfaces.CustomerService;
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
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, DtoMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    @Override
    public Customer findOne(Integer customerId) {
       return customerRepository.findById(customerId)
               .orElseThrow(() -> new CustomerEntityNotFoundException(Customer.class,"id", customerId.toString()));
    }

    @Override
    public Customer add(Customer customer) throws EmailDuplicateException {
        Boolean emailExists = customerRepository.emailAlreadyExists(customer.getEmail());
        if(Boolean.TRUE.equals(emailExists)) {
            throw new EmailDuplicateException("Email bestaat al in het systeem!");
        }


       Customer newCustomer = customerRepository.save(customer);
        log.info(String.format("Klant aangemaakt met klant-id: %s",
                newCustomer.getId()));
       return newCustomer;
    }

    @Override
    public void update(Integer customerId, CustomerUpdateDto customerUpdateDto) {
               Customer existingCustomer = customerRepository.findById(customerId)
               .orElseThrow(() -> new CustomerEntityNotFoundException(
                       Customer.class,"id",
                       customerId.toString())
               );

               mapper.updateCustomerFromDto(customerUpdateDto,existingCustomer);
               customerRepository.save(existingCustomer);
    }

    @Override
    public void delete(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerEntityNotFoundException(
                        Customer.class,
                        "id",
                        customerId.toString())
                );
        customerRepository.delete(customer);
    }
    @Override
    public List<Customer> getCustomerCallingList() {
        return customerRepository.getCallingList();
    }
}
