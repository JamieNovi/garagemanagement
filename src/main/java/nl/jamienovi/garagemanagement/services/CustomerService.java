package nl.jamienovi.garagemanagement.services;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerUpdateDto;

import java.util.List;

public interface CustomerService extends GenericService<Customer,Integer,CustomerUpdateDto>{
    List<Customer> getCustomerCallingList();
}
