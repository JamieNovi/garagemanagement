package nl.jamienovi.garagemanagement.customer;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import nl.jamienovi.garagemanagement.interfaces.CustomerService;
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
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    @GetMapping(path = "/{customerId}")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerId") int customerId) {
        Customer customer= customerService.findOne(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(path = "contact")
    @PreAuthorize("hasAnyAuthority('customer:read')")
    public List<Customer> getCustomerCallingList() {
        return customerService.getCustomerCallingList();
    }

    @PostMapping(path = "")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<URI> addCustomer(@Valid @RequestBody Customer customer) {
        Customer customerId = customerService.add(customer);
        log.info(customerId.toString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId.getId())
                .toUri();
        return ResponseEntity.created(uri).body(uri);
    }

    @PutMapping(path="/{customerId}")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<URI> updateCustomer(@PathVariable("customerId") Integer customerId,
                                            @RequestBody CustomerUpdateDto customerUpdateDto,
                                            UriComponentsBuilder uriComponentsBuilder) {
        customerService.update(customerId , customerUpdateDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/klanten/{id}").buildAndExpand(customerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @DeleteMapping(path = "/{customerId}")
    @PreAuthorize("hasAuthority('customer:write')")
    public ResponseEntity<ResponseMessage> deleteCustomer(@PathVariable("customerId") int customerId){
        customerService.delete(customerId);
        return ResponseEntity.ok(
                new ResponseMessage("Klant verwijderd"));
    }
}
