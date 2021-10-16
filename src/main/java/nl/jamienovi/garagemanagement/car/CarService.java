package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CarService {

   private final CarRepository carRepository;
   private final CustomerService  customerService;
   private final DtoMapper mapper;

   @Autowired
   public CarService(CarRepository carRepository, CustomerService customerService,
                     DtoMapper mapper) {
        this.carRepository = carRepository;
        this.customerService = customerService;
       this.mapper = mapper;
   }

    public List<Car> getAllCars() {
       return carRepository.findAll();
    }

    public Car getCar(int carId){
       Optional<Car> carOptional = carRepository.findById(carId);
       if(carOptional.isEmpty())  {
           throw new IllegalStateException("Auto bestaat niet");
       }
       return carOptional.get();
    }

    public void addCarToCustomer(int customerId,Car newCar) {
       Customer customer = customerService.getCustomer(customerId);
       customer.getCars().add(newCar);
       newCar.setCustomer(customer);
       carRepository.save(newCar);
    }

    public void updateCarCustomer(int carId, CarDto carDto){
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalStateException(
                        "Car met id " + carDto.getId() + " bestaat niet."
                ));
        mapper.updateCarFromDto(carDto,existingCar);
        carRepository.save(existingCar);
    }

    public void deleteCar(int carId){
       Optional<Car> carOptional = carRepository.findById(carId);
       if(!carOptional.isPresent()){
           log.info("Auto bestaat niet");
           throw new IllegalStateException("Auto bestaat niet");
       }
       carRepository.deleteById(carId);
    }


}
