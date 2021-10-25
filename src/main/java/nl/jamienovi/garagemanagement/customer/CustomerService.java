package nl.jamienovi.garagemanagement.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer getCustomer(Integer customerId) {
       return customerRepository.findById(customerId)
               .orElseThrow(() -> new EntityNotFoundException(Customer.class,"id", customerId.toString()));
    }

    public Customer saveCustomer(Customer customer) {
        if(customerRepository.emailAlreadyExists(customer.getEmail())) {
            throw new IllegalStateException("Email bestaat al in het systeem!");
        }
       return customerRepository.save(customer);
    }

    public void updateCustomer(Integer customerId, CustomerDto customerDto) {
               Customer existingCustomer = customerRepository.findById(customerId)
               .orElseThrow(() -> new EntityNotFoundException(Customer.class,"id", customerId.toString()));

               mapper.updateCustomerFromDto(customerDto,existingCustomer);
               customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Integer customerId) {
        boolean exists = customerRepository.existsById(customerId);
        if(!exists) {
            throw new EntityNotFoundException(Customer.class,"id", customerId.toString());

        }
        customerRepository.deleteById(customerId);
    }

    public List<Customer> getCallingListOfCustomersWithStatusVoltooidOrNietUitvoeren() {
        return customerRepository.getCallingList();
    }
}
