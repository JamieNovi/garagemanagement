package nl.jamienovi.garagemanagement.car;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private int id;
    private String brand;
    private String model;
    private String registrationPlate;
}
