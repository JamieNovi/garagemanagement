package nl.jamienovi.garagemanagement.car;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import nl.jamienovi.garagemanagement.interfaces.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @GetMapping(path = "/auto")
    @PreAuthorize("hasAnyAuthority('car:read','car:write')")
    public List<Car> getAllCars() {
        return carService.findAll();
    }

    @GetMapping(path = "/auto/{carId}")
    @PreAuthorize("hasAnyAuthority('car:read','car:write')")
    public Car getCar(@PathVariable("carId") int carId) {
        return carService.findOne(carId);
    }

    @PreAuthorize("hasAuthority('car:write')")
    @PostMapping(path = "/auto/{customerId}")
    public ResponseEntity<URI> addCar(@PathVariable("customerId") Integer customerId,
                                      @RequestBody Car car,
                                      UriComponentsBuilder uriComponentsBuilder){
        Integer carId = carService.addCarToCustomer(customerId,car);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/auto/{id}")
                .buildAndExpand(carId);

        return ResponseEntity.created(uriComponents.toUri()).body(uriComponents.toUri());
    }


    @PutMapping(path = "/auto/{carId}")
    @PreAuthorize("hasAuthority('car:write')")
    public ResponseEntity<URI> updateCar(@PathVariable("carId") Integer carId,
                                         @RequestBody CarDto carDto,
                                         UriComponentsBuilder uriComponentsBuilder){
        carService.update(carId,carDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/auto/{id}")
                .buildAndExpand(carId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return ResponseEntity.ok().headers(headers).body(uriComponents.toUri());
    }

    @DeleteMapping(path = "/auto/{customerId}")
    @PreAuthorize("hasAuthority('car:write')")
       public ResponseEntity<ResponseMessage> deleteCar(@PathVariable("customerId") int customerId) {
           carService.delete(customerId);

           return ResponseEntity.ok(
                   new ResponseMessage(String.format("Auto met id %s is verwijderd",customerId))
           );
    }

}
