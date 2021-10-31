package nl.jamienovi.garagemanagement.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.car.Car;

import java.util.List;

@Getter
@Setter
@ToString
public class CustomerGetDto {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String postalCode;
    private String city;
    private List<Car> cars;
}
