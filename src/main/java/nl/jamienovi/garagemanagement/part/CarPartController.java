package nl.jamienovi.garagemanagement.part;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/auto-onderdelen")
public class CarPartController {

    private final CartPartService cartPartService;

    @Autowired
    public CarPartController(CartPartService cartPartService) {
        this.cartPartService = cartPartService;
    }

    @GetMapping(path ="/")
    public List<Part> getCarParts(){
        return cartPartService.getAllCarParts();

    }

    @GetMapping(path = "/{carPartId}")
    public Part getCarPart(@PathVariable("carPartId") Integer carPartId){
        return cartPartService.getCarPart(carPartId);
    }


}
