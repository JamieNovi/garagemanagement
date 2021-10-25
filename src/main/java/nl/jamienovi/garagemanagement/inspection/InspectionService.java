package nl.jamienovi.garagemanagement.inspection;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.car.CarRepository;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.AddApprovalStatusEvent;
import nl.jamienovi.garagemanagement.eventmanager.ChangeInspectionStatusEvent;
import nl.jamienovi.garagemanagement.eventmanager.EventManager;
import nl.jamienovi.garagemanagement.eventmanager.AddInspectionReportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
        }

        InspectionReport newInspectionReport = new InspectionReport();
        newInspectionReport.setCar(existingCar);
        inspectionReportRepository.save(newInspectionReport);
        log.info(
                String.format("Keuringsrapport aangemaakt auto met id : %s van klant-id: %s toegevoegd",
                        existingCar.getId(),existingCar.getCustomer().getId())
        );

        //Publiseert event zodat er een reparatie wordt aangemaakt in RepairOrderService
        AddInspectionReportEvent event = new AddInspectionReportEvent(this,carId);
        applicationEventPublisher.publishEvent(event);
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

        //Zet reparatie op voltooid als auto is goedgekeurd. Repairorder wordt op VOLTOOID gezet.
        if(status == InspectionStatus.GOEDGEKEURD) {
            ChangeInspectionStatusEvent event = new ChangeInspectionStatusEvent(this,inspectionReportId );
            applicationEventPublisher.publishEvent(event);

            log.info(String.format("Auto met klant-id: %s en auto-id %s is %s",
                    report.getCar().getCustomer().getId(),
                    report.getCar().getId(),
                    status));
        }else {
            log.info("Auto van klant-id: "+
                    report.getCar().getCustomer().getId() + " is : " + status.toString() + ". Contact opnemen met klant");
        }

    }

    public void setApprovalRepair(Integer inspectionReportId, ApprovalStatus status) {
        InspectionReport report = inspectionReportRepository.getById(inspectionReportId);
        report.setApprovalStatus(status);

        inspectionReportRepository.save(report);
        if(status == ApprovalStatus.NIETAKKOORD){

            AddApprovalStatusEvent event = new AddApprovalStatusEvent(this, inspectionReportId);
            applicationEventPublisher.publishEvent(event);

            log.info(setApprovalLogMessage(report,status));

        }else{
            log.info("Reparatie met id: " + report.getRepairOrder().getId() + " is : " + status.toString());
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

                if (item.getStatus() == InspectionStatus.IN_BEHANDELING) {
                    carIsPending = true;
                }
            }
        return carIsPending;
    }

    private String setApprovalLogMessage(InspectionReport report, ApprovalStatus status){
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
}
