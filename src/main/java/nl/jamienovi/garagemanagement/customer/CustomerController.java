package nl.jamienovi.garagemanagement.customer;

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

    @GetMapping(path = "/klanten")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/klant/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") int customerId){
        return customerService.getCustomer(customerId);
    }

    @PostMapping(path = "/klant-toevoegen")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return ResponseEntity.ok(
                new MessageResponse(String.format("Klant succesvol aangemaakt.")));

    }

    @PutMapping(path="/klant-aanpassen/{customerId}")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerDto);
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
