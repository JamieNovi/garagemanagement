package nl.jamienovi.garagemanagement.inspection;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
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
        return inspectionReportRepository.findAll();
    }

    public InspectionReport getSingleInspectionReport(Integer inspectionReportId){
        return inspectionReportRepository.getById(inspectionReportId);
    }

    public void addInspectionReportToCar(Integer carId){
        InspectionReport newInspectionReport = new InspectionReport();
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Car.class,"id",carId.toString()
                ));
        if(hasPendingStatus(existingCar.getId())){
            log.info("Auto heeft al een keuring openstaan");
            throw new IllegalStateException("Auto heeft al een keuring openstaan");
        }
        newInspectionReport.setCar(existingCar);
        inspectionReportRepository.save(newInspectionReport);
        repairOrderService.addRepairOrder(carId);
    }

    public void deleteInspectionReport(Integer inspectionReportId){
        InspectionReport inspectionReport = inspectionReportRepository.findById(inspectionReportId)
                .orElseThrow(() -> new EntityNotFoundException(
                        InspectionReport.class,"id",inspectionReportId.toString()
                ));
        inspectionReportRepository.delete(inspectionReport);
    }

    public void setInspectionReportStatus(Integer id,InspectionStatus status){
        InspectionReport report = inspectionReportRepository.getById(id);
        report.setStatus(status);
        inspectionReportRepository.save(report);
    }

    /**
     * Controleerd of een auto al een keuring heeft openstaan,
     * voordat er een nieuwr keuring kan worden aangemaakt. Pending=in afwachting.
     * @param carId
     * @return Boolean true of false
     */
    public Boolean hasPendingStatus(Integer carId) {
        Car car = carRepository.getById(carId);
        Boolean carIsPending = false;

        for (InspectionReport item : car.getInspectionReports()) {

                if (item.getStatus() == InspectionStatus.IN_BEHANDELING) {
                    carIsPending = true;
                }
            }
        return carIsPending;
    }

}
