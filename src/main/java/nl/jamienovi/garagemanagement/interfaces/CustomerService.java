package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerUpdateDto;

import java.util.List;

public interface CustomerService extends GenericService<Customer,Integer,CustomerUpdateDto>{
    List<Customer> getCustomerCallingList();
}
