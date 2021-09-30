package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class CarController {
    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(path = "/voertuigen")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping(path = "/voertuig/{carId}")
    public Car getCar(@PathVariable("carId") int carId) {
        return carService.getCar(carId);
    }

    @PostMapping(path = "/voertuig-toevoegen/{customerId}")
    public ResponseEntity<?> addCar(@PathVariable("customerId") int customerId, @RequestBody Car car){
        carService.addCarToCustomer(customerId,car);
        return ResponseEntity.ok(
                new MessageResponse("Auto succesvol aangemaakt.")
        );
    }

    @PutMapping(path = "/voertuig-aanpassen/{carId}")
    public ResponseEntity<?> updateCar(@PathVariable("carId") int carId, @RequestBody CarDto carDto){
        carService.updateCarCustomer(carId,carDto);
        return ResponseEntity.ok(
                new MessageResponse("Auto met id" + carId + " is aangepast.")
        );
    }

    @DeleteMapping(path = "/voertuig-verwijderen/{customerId}")
       public ResponseEntity<?> deleteCar(@PathVariable("customerId") int customerId) {
           carService.deleteCar(customerId);
           return ResponseEntity.ok(
                   new MessageResponse(String.format("Auto met id %s is verwijderd",customerId))
           );
    }
}
