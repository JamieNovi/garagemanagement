package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionService {
    private final InspectionReportRepository inspectionReportRepository;
    private final CarRepository carRepository;
    private final RepairOrderService repairOrderService;

    @Autowired
    public InspectionService(InspectionReportRepository inspectionReportRepository,
                             CarRepository carRepository, RepairOrderService repairOrderService) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.carRepository = carRepository;
        this.repairOrderService = repairOrderService;
    }

    public List<InspectionReport> getAllInspectionReports() {
        return inspectionReportRepository.findAll();
    }

    public InspectionReport getSingleInspectionReport(Integer inspectionReportId){
        return inspectionReportRepository.getById(inspectionReportId);
    }

    public void addInspectionReportToCar(int carId, InspectionReport newInspectionReport){
        Optional<Car> car = carRepository.findById(carId);
        newInspectionReport.setCar(car.get());
        inspectionReportRepository.save(newInspectionReport);

    }


}
