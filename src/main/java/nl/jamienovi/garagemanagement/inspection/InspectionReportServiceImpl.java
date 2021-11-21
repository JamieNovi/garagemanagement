package nl.jamienovi.garagemanagement.inspection;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.customer.CustomerServiceImpl;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.AddApprovalStatusEvent;
import nl.jamienovi.garagemanagement.eventmanager.AddInspectionReportEvent;
import nl.jamienovi.garagemanagement.eventmanager.ChangeInspectionStatusEvent;
import nl.jamienovi.garagemanagement.eventmanager.RepairOrderCompletedEvent;
import nl.jamienovi.garagemanagement.services.CarService;
import nl.jamienovi.garagemanagement.services.InspectionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Class represents bussines logic for all operations related to InspectionReport
 *
 * @version 1.1 20 Sept 2021
 * @author Jamie Spekman
 */
@Slf4j
@Service
@Transactional
public class InspectionReportServiceImpl implements InspectionReportService {
    private final InspectionReportRepository inspectionReportRepository;
    private final CarService carService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public InspectionReportServiceImpl(InspectionReportRepository inspectionReportRepository,
                                       CarRepository carRepository,
                                       CustomerServiceImpl customerServiceImpl,
                                       CarService carService,
                                       ApplicationEventPublisher applicationEventPublisher) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.carService = carService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<InspectionReport> getAllInspectionReports() {
        return inspectionReportRepository.findAll();
    }

    @Override
    public InspectionReport getInspectionReport(Integer inspectionReportId){
        return inspectionReportRepository.getById(inspectionReportId);
    }

    @Override
    public void addInspectionReport(Integer carId){
        Car existingCar = carService.findOne(carId);

        if(hasPendingStatus(existingCar.getId())){ // check if car has pending inspectionreport
            throw new IllegalStateException("Auto heeft al een keuring openstaan");
        }else {
            InspectionReport newInspectionReport = new InspectionReport();
            newInspectionReport.setCar(existingCar);
            newInspectionReport = inspectionReportRepository.save(newInspectionReport);
            log.info(
                    String.format("Keuringsrapport-id: %s aangemaakt auto met id : %s van klant-id: %s",
                            newInspectionReport.getId(),
                            existingCar.getId(),
                            existingCar.getCustomer().getId())
            );

            /*
             * Publish event for RepairOrderService to create repairorder
             * after inspectionreport is created
             */

            AddInspectionReportEvent event = new AddInspectionReportEvent(this,carId);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void deleteInspectionReport(Integer inspectionReportId){
        InspectionReport inspectionReport = inspectionReportRepository.findById(inspectionReportId)
                .orElseThrow(() -> new EntityNotFoundException(
                        InspectionReport.class,"id",inspectionReportId.toString()
                ));
        inspectionReportRepository.delete(inspectionReport);
    }

    @Override
    public void setInspectionReportStatus(Integer inspectionReportId, InspectionStatus status){
        InspectionReport report = inspectionReportRepository.getById(inspectionReportId);
        report.setStatus(status);
        inspectionReportRepository.save(report);

        //Stuur event naar repairorder om status te wijzigen naar VOLTOOID

        switch(status) {
            case GOEDGEKEURD:
                // Publish event for RepairOrderService to change status to NIET_UITVOEREN
                ChangeInspectionStatusEvent eventInspectionStatus = new ChangeInspectionStatusEvent(this,inspectionReportId );
                applicationEventPublisher.publishEvent(eventInspectionStatus);

                log.info(String.format("Auto met klant-id: %s en auto-id %s is %s",
                        report.getCar().getCustomer().getId(),
                        report.getCar().getId(),
                        status));
                break;
            case AFGEKEURD:
                log.info("Auto van klant-id: "+
                        report.getCar().getCustomer().getId() + " is : " + status.toString() + ". Contact opnemen met klant");
        }
    }

    @Override
    public void setIsRepaired(Integer inspectionReportId) {
        InspectionReport updateInspectionReport = inspectionReportRepository
                .getById(inspectionReportId);
        updateInspectionReport.setIsRepaired(true);
        inspectionReportRepository.save(updateInspectionReport);
    }

    @Override
    public void setApprovalRepair(Integer inspectionReportId, RepairApprovalStatus status) {
        InspectionReport report = inspectionReportRepository.getById(inspectionReportId);
        report.setRepairApprovalStatus(status);
        inspectionReportRepository.save(report);
        if(status == RepairApprovalStatus.NIETAKKOORD){
            AddApprovalStatusEvent event = new AddApprovalStatusEvent(
                    this,
                    inspectionReportId,
                    RepairApprovalStatus.NIETAKKOORD);
            applicationEventPublisher.publishEvent(event);
        }else{
            log.info("Keuringsrapport-id: " + report.getId() + " is : " + status.toString()+ " met reparatie.");
            AddApprovalStatusEvent event = new AddApprovalStatusEvent(
                    this,
                    inspectionReportId,
                    RepairApprovalStatus.AKKOORD);
            applicationEventPublisher.publishEvent(event);
        }
    }

    /**
     *
     * Checks if car has a pending inspection before mechanic adds a new inspectionreport
     * @param carId
     * @return Boolean true of false
     */
    @Override
    public Boolean hasPendingStatus(Integer carId) {
        Car car = carService.findOne(carId);
        Boolean carIsPending = false;

        for (InspectionReport item : car.getInspectionReports()) {
                switch(item.getStatus()){
                    case IN_BEHANDELING:
                        carIsPending = true;
                        break;
                    case AFGEKEURD:
                        carIsPending = false;
                        break;
                    case GOEDGEKEURD:
                        carIsPending = false;
                }
            }
        return carIsPending;
    }

    @Override
    @EventListener
    public void handleRepairOrderStatusEvent(RepairOrderCompletedEvent event) {
        setIsRepaired(event.getInspectionReportId());
    }
}
