package nl.jamienovi.garagemanagement.inspection;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.errorhandling.PendingInspectionReportException;
import nl.jamienovi.garagemanagement.eventmanager.ApprovalStatusChangeEvent;
import nl.jamienovi.garagemanagement.eventmanager.InspectionReportCreatedEvent;
import nl.jamienovi.garagemanagement.eventmanager.InspectionStatusChangeEvent;
import nl.jamienovi.garagemanagement.eventmanager.RepairOrderStatusChangeEvent;
import nl.jamienovi.garagemanagement.interfaces.CarService;
import nl.jamienovi.garagemanagement.interfaces.InspectionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static nl.jamienovi.garagemanagement.inspection.InspectionStatus.AFGEKEURD;
import static nl.jamienovi.garagemanagement.inspection.InspectionStatus.GOEDGEKEURD;

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

    /**
     * Save inspectionreport with id of car.
     * @param carId
     * @throws PendingInspectionReportException
     */
    @Override
    public void addInspectionReport(Integer carId) throws PendingInspectionReportException {
        Car existingCar = carService.findOne(carId);
        Boolean isPending = hasPendingStatus(existingCar.getId());
        // check if car has pending inspectionreport
        if(Boolean.TRUE.equals(isPending)){
            throw new PendingInspectionReportException(
                    "Auto heeft al een keuring openstaan. Status van keuringsrapport is : IN BEHANDELING. "
            );
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

            InspectionReportCreatedEvent event = new InspectionReportCreatedEvent(this,carId);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public void deleteInspectionReport(Integer inspectionReportId){
        InspectionReport inspectionReport = inspectionReportRepository.findById(inspectionReportId)
                .orElseThrow(() -> new CustomerEntityNotFoundException(
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
        if(status == GOEDGEKEURD) {
            // Publish event for RepairOrderService to change status to NIET_UITVOEREN
            InspectionStatusChangeEvent eventInspectionStatus =
                    new InspectionStatusChangeEvent(this,inspectionReportId );
            applicationEventPublisher.publishEvent(eventInspectionStatus);

            log.info(String.format("Auto met klant-id: %s en auto-id %s is %s",
                    report.getCar().getCustomer().getId(),
                    report.getCar().getId(),
                    status));
        }else if (status == AFGEKEURD) {
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
            ApprovalStatusChangeEvent event = new ApprovalStatusChangeEvent(
                    this,
                    inspectionReportId,
                    RepairApprovalStatus.NIETAKKOORD);
            applicationEventPublisher.publishEvent(event);
        }else{
            log.info("Keuringsrapport-id: " + report.getId() + " is : " + status.toString()+ " met reparatie.");
            ApprovalStatusChangeEvent event = new ApprovalStatusChangeEvent(
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
    public void handleRepairOrderStatusEvent(RepairOrderStatusChangeEvent event) {
        setIsRepaired(event.getInspectionReportId());
    }
}
