package nl.jamienovi.garagemanagement.services;

import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarDto;

public interface CarService extends GenericService<Car,Integer, CarDto>{
    Integer addCarToCustomer(Integer customerId,Car car);
}
