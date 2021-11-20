package nl.jamienovi.garagemanagement.customer;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/klanten")
public class CustomerController {
    private CustomerServiceImpl customerServiceImpl;

    @Autowired
    public CustomerController(CustomerServiceImpl customerServiceImpl) {
        this.customerServiceImpl = customerServiceImpl;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public List<Customer> getAllCustomers() {
        return customerServiceImpl.findAll();
    }

    @GetMapping(path = "/{customerId}")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") int customerId) throws EntityNotFoundException {
        Customer customer= customerServiceImpl.findOne(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(path = "contact")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public List<Customer> getCustomerCallingList() {
        return customerServiceImpl.getCustomerCallingList();
    }

    @PostMapping(path = "")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) throws IllegalStateException{
        Customer customerId = customerServiceImpl.add(customer);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId.getId())
                .toUri();
        return ResponseEntity.created(uri).body(uri);
    }

    @PutMapping(path="/{customerId}")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<?> updateCustomer(@PathVariable("customerId") Integer customerId,
                                            @RequestBody CustomerUpdateDto customerUpdateDto,
                                            UriComponentsBuilder uriComponentsBuilder) {
        customerServiceImpl.update(customerId , customerUpdateDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/klanten/{id}").buildAndExpand(customerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @DeleteMapping(path = "/{customerId}")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") int customerId){
        customerServiceImpl.delete(customerId);
        return ResponseEntity.ok(
                new ResponseMessage(String.format("Klant verwijderd.")));
    }
}
