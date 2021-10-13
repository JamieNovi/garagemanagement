package nl.jamienovi.garagemanagement.part;

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

    public void addPart(Part carpart) {
        carPartRepository.save(carpart);   
    }

    public Part getCarPart(Integer carPartId) {
        Part part = carPartRepository.getById(carPartId);
        //log.info(part.calculateTax().toString());
        return part;
    }

    public List<Part> getAllCarParts() {
        return carPartRepository.findAll();
    }
}
