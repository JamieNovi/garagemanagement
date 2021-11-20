package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerServiceImpl;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
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
public class CarService {

   private final CarRepository carRepository;
   private final CustomerServiceImpl customerServiceImpl;

   @Autowired
   public CarService(CarRepository carRepository, CustomerServiceImpl customerServiceImpl) {
        this.carRepository = carRepository;
        this.customerServiceImpl = customerServiceImpl;
   }

    public List<Car> getAllCars() {
       return carRepository.findAll();
    }

    public Car getCar(Integer carId){
       return carRepository.findById(carId)
               .orElseThrow(() ->
                   new EntityNotFoundException(Car.class,"id",carId.toString()));
    }

    public Integer addCarToCustomer(Integer customerId,Car newCar) {
       Customer customer = customerServiceImpl.findOne(customerId);
       newCar.setCustomer(customer);
       Car car = carRepository.save(newCar);
       log.info("Auto-id : {} aangemaakt voor klant-id: {}",
               car.getId(),customerId);
       return car.getId();
    }

    public void updateCarCustomer(Integer carId, CarDto carDto){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() ->
                        new EntityNotFoundException(Car.class,"id",carId.toString()));
        DtoMapper.INSTANCE.updateCarFromDto(carDto,existingCar); // map carDto to car
        carRepository.save(existingCar);
    }

    public void deleteCar(Integer carId){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() ->
                        new EntityNotFoundException(Car.class,"id",carId.toString()));
        log.info(existingCar.getId().toString());
       carRepository.deleteById(1);
    }
}
