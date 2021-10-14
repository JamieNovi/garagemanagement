package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
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
    private final CustomerService customerService;

    @Autowired
    public InspectionService(InspectionReportRepository inspectionReportRepository,
                             CarRepository carRepository,
                             RepairOrderService repairOrderService,
                             CustomerService customerService) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.carRepository = carRepository;
        this.repairOrderService = repairOrderService;
        this.customerService = customerService;
    }

    public List<InspectionReport> getAllInspectionReports() {
        return inspectionReportRepository.getAllInspectionReports();
    }

    public InspectionReport getSingleInspectionReport(Integer inspectionReportId){
        return inspectionReportRepository.getById(inspectionReportId);
    }

    public void addInspectionReportToCar(int carId, InspectionReport newInspectionReport){
        Optional<Car> car = carRepository.findById(carId);
        Customer customerOfCar = customerService.getCustomer(car.get().getCustomer().getId());
        newInspectionReport.setCar(car.get());
        //newInspectionReport.setRepairOrder(new RepairOrder(customerOfCar));
        inspectionReportRepository.save(newInspectionReport);

    }

    public void deleteInspectionReport(Integer inspectionReportId){
        InspectionReport inspectionReport = inspectionReportRepository.findById(inspectionReportId)
                .orElseThrow(() -> new EntityNotFoundException(
                        InspectionReport.class,"id",inspectionReportId.toString()
                ));
        inspectionReportRepository.delete(inspectionReport);
    }


}
