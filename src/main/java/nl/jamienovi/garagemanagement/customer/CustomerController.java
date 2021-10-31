package nl.jamienovi.garagemanagement.customer;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/klanten")
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") int customerId) throws EntityNotFoundException {
        Customer customer= customerService.getCustomer(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(path = "opbellen")
    public List<Customer> getCustomerCallingList() {
        return customerService.getCallingListOfCustomersWithStatusVoltooidOrNietUitvoeren();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) throws IllegalStateException{
        Customer newCustomer = customerService.saveCustomer(customer);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();
        return ResponseEntity.created(uri).body(uri);
    }

    @PutMapping(path="/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable("customerId") Integer customerId,
                                            @RequestBody CustomerUpdateDto customerUpdateDto,
                                            UriComponentsBuilder uriComponentsBuilder) {
        customerService.updateCustomer(customerId , customerUpdateDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/klanten/{id}").buildAndExpand(customerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") int customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(
                new ResponseMessage(String.format("Klant verwijderd.")));
    }
}
