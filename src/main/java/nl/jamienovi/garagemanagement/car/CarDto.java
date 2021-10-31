package nl.jamienovi.garagemanagement.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.jamienovi.garagemanagement.customer.Customer;

@Getter
@Setter
@AllArgsConstructor
public class CarDto {
    private int id;
    private String brand;
    private String model;
    private String registrationPlate;
    private Customer customer;

}
