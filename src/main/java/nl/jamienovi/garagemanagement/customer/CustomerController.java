package nl.jamienovi.garagemanagement.customer;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/klanten")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/klanten/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") int customerId) throws EntityNotFoundException {
        return customerService.getCustomer(customerId);
    }

    @PostMapping(path = "/klant-toevoegen")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) throws IllegalStateException{
        customerService.saveCustomer(customer);
        return ResponseEntity.ok(
                new MessageResponse(String.format("Klant succesvol aangemaakt.")));

    }

    @PutMapping(path="/klant-aanpassen/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customerId") Integer customerId, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerId ,customerDto);
        return ResponseEntity.ok(
                new MessageResponse(String.format("Klant succesvol aangepast.")));
    }


    @DeleteMapping(path = "/klant-verwijderen/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") int customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(
                new MessageResponse(String.format("Klant verwijderd.")));
    }
}
