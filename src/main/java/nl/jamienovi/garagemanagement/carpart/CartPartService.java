package nl.jamienovi.garagemanagement.carpart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartPartService {
    private final CarPartRepository carPartRepository;

    @Autowired
    public CartPartService(CarPartRepository carPartRepository) {
        this.carPartRepository = carPartRepository;
    }

    public void addPart(CarPart carpart) {
        carPartRepository.save(carpart);   
    }

    public CarPart getCarPart(Integer carPartId) {
        CarPart part = carPartRepository.getById(carPartId);
        //log.info(part.calculateTax().toString());
        return part;
    }

    public List<CarPart> getAllCarParts() {
        return carPartRepository.findAll();
    }
}
