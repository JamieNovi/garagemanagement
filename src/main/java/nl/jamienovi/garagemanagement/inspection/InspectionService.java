package nl.jamienovi.garagemanagement.inspection;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class InspectionService {
    private final InspectionReportRepository inspectionReportRepository;
    private final CarRepository carRepository;
    private final EventManager events;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public InspectionService(InspectionReportRepository inspectionReportRepository,
                             CarRepository carRepository,
                             CustomerService customerService,
                             ApplicationEventPublisher applicationEventPublisher) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.carRepository = carRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.events = new EventManager("GOEDGEKEURD", "NIET_UITVOEREN");
    }

    public List<InspectionReport> getAllInspectionReports() {
        return inspectionReportRepository.findAll();
    }

    public InspectionReport getSingleInspectionReport(Integer inspectionReportId){
        return inspectionReportRepository.getById(inspectionReportId);
    }

    public void addInspectionReportToCar(Integer carId){

        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Car.class,"id",carId.toString()
                ));

        if(hasPendingStatus(existingCar.getId())){
            log.info("Auto heeft al een keuring openstaan");
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
            // == EVENT voor reparatieservice, aanmaken reparatie Order ==

            AddInspectionReportEvent event = new AddInspectionReportEvent(this,carId);
            applicationEventPublisher.publishEvent(event);
        }

    }

    public void deleteInspectionReport(Integer inspectionReportId){
        InspectionReport inspectionReport = inspectionReportRepository.findById(inspectionReportId)
                .orElseThrow(() -> new EntityNotFoundException(
                        InspectionReport.class,"id",inspectionReportId.toString()
                ));
        inspectionReportRepository.delete(inspectionReport);
    }

    public void setInspectionReportStatus(Integer inspectionReportId,InspectionStatus status){

        InspectionReport report = inspectionReportRepository.getById(inspectionReportId);
        report.setStatus(status);
        inspectionReportRepository.save(report);

        //Stuur event naar repairorder om status te wijzigen naar VOLTOOID

        switch(status) {
            case GOEDGEKEURD:
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

    public void setIsRepaired(Integer inspectionReportId) {
        InspectionReport updateInspectionReport = inspectionReportRepository.getById(inspectionReportId);
        updateInspectionReport.setIsRepaired(true);
        inspectionReportRepository.save(updateInspectionReport);
    }

    public void setApprovalRepair(Integer inspectionReportId, RepairApprovalStatus status) {
        InspectionReport report = inspectionReportRepository.getById(inspectionReportId);
        Integer carIdOfInspectionReport = report.getCar().getId();

        report.setRepairApprovalStatus(status);

        inspectionReportRepository.save(report);
        if(status == RepairApprovalStatus.NIETAKKOORD){

            AddApprovalStatusEvent event = new AddApprovalStatusEvent(
                    this,
                    inspectionReportId,
                    RepairApprovalStatus.NIETAKKOORD);
            applicationEventPublisher.publishEvent(event);

//            ChangeCarStatusEvent eventCarStatus = new ChangeCarStatusEvent(
//                    this,carIdOfInspectionReport);
//            applicationEventPublisher.publishEvent(eventCarStatus);

           // log.info(setApprovalLogMessage(report,status));

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
     * Controleert of een auto al een keuring heeft openstaan,
     * voordat er een nieuwe keuring kan worden aangemaakt. Pending=in afwachting.
     * @param carId
     * @return Boolean true of false
     */
    public Boolean hasPendingStatus(Integer carId) {
        Car car = carRepository.getById(carId);
        Boolean carIsPending = false;

        for (InspectionReport item : car.getInspectionReports()) {

                switch(item.getStatus()){
                    case IN_BEHANDELING:
                        carIsPending = true;
                        break;
                    case AFGEKEURD:
                        carIsPending = true;
                        break;
                    case GOEDGEKEURD:
                        carIsPending = true;

                }
            }
        return carIsPending;
    }

    private String setApprovalLogMessage(InspectionReport report, RepairApprovalStatus status){
        return String.format(
                "Klant-id:%s met naam: %s, auto-id: %s, keuringsrapport-id: %s en reparatie-id: %s gaat niet %s met de reparatie\n",
                report.getCar().getCustomer().getId(),
                report.getCar().getCustomer().getFirstName(),
                report.getCar().getId(),
                report.getId(),
                report.getRepairOrder().getId(),
                status.toString()
        );
    }

//    private String inspectionReportStatusLogMessage(InspectionReport, InspectionStatus){
//        return String.format()
//
//    }
    public EventManager getEvents() {
        return events;
    }

    @EventListener
    public void handleRepairOrderStatusEvent(RepairOrderCompletedEvent event) {
        setIsRepaired(event.getInspectionReportId());
    }
}
