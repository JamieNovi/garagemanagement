package nl.jamienovi.garagemanagement.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
