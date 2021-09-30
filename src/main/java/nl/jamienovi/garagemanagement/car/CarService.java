package nl.jamienovi.garagemanagement.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

   private final CarRepository carRepository;

   @Autowired
   public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
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

}
