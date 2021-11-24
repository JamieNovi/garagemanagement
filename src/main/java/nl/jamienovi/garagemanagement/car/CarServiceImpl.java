package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.interfaces.CarService;
import nl.jamienovi.garagemanagement.interfaces.CustomerService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class handles business logic for car operations
 * @version 1.3 11 Sept 2021
 * @author Jamie spekman
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {
   private final CarRepository carRepository;
   private final CustomerService customerService;

   @Autowired
   public CarServiceImpl(CarRepository carRepository, CustomerService customerService) {
        this.carRepository = carRepository;
        this.customerService = customerService;
   }

   @Override
    public List<Car> findAll() {
       return carRepository.findAll();
    }

    @Override
    public Car findOne(Integer carId){
       return carRepository.findById(carId)
               .orElseThrow(() ->
                   new CustomerEntityNotFoundException(Car.class,"id",carId.toString()));
    }

    @Override
    public Integer addCarToCustomer(Integer customerId,Car newCar) {
       Customer customer = customerService.findOne(customerId);
       newCar.setCustomer(customer);
       Car car = carRepository.save(newCar);
       log.info("Auto-id : {} aangemaakt voor klant-id: {}",
               car.getId(),customerId);
       return car.getId();
    }

    @Override
    public void update(Integer carId, CarDto carDto){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() ->
                        new CustomerEntityNotFoundException(Car.class,"id",carId.toString()));
        DtoMapper.INSTANCE.updateCarFromDto(carDto,existingCar); // map carDto to car
        carRepository.save(existingCar);
    }

    @Override
    public void delete(Integer carId){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() ->
                        new CustomerEntityNotFoundException(Car.class,"id",carId.toString()));
       carRepository.deleteById(existingCar.getId());
    }
}
