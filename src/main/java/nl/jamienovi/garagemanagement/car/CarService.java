package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarService {

   private final CarRepository carRepository;
   private final CustomerService  customerService;

   @Autowired
   public CarService(CarRepository carRepository, CustomerService customerService) {
        this.carRepository = carRepository;
        this.customerService = customerService;
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
       Customer customer = customerService.getCustomer(customerId);
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
        DtoMapper.INSTANCE.updateCarFromDto(carDto,existingCar);
        carRepository.save(existingCar);
    }

    public void deleteCar(Integer carId){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() ->
                        new EntityNotFoundException(Car.class,"id",carId.toString()));
       carRepository.deleteById(existingCar.getId());
    }
}
