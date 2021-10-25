package nl.jamienovi.garagemanagement.repairorder;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.*;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RepairOrderService {
    private final RepairOrderRepository repairOrderRepository;
    private final CustomerService customerService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventManager events;

    @Autowired
    public RepairOrderService(RepairOrderRepository repairOrderRepository,
                              CustomerService customerService,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.repairOrderRepository = repairOrderRepository;
        this.customerService = customerService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.events = new EventManager("VOLTOOID","NIET_UITVOEREN");
    }

    public List<RepairOrder> getAll() {
        return repairOrderRepository.findAll();
    }

    public RepairOrder getSingle(Integer repairOrderId) {
        RepairOrder repairOrder = repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() ->  new EntityNotFoundException(
                        RepairOrder.class,"id", repairOrderId.toString())
                );

        return repairOrder;
    }

    public void addRepairOrder(Integer carId) {
        Optional<InspectionReport> inspectionReport = repairOrderRepository.getInspectionReportFromCustomer(carId);
        Customer customer = customerService.getCustomer(inspectionReport.get().getCar().getCustomer().getId());

        RepairOrder newRepairOrder = new RepairOrder(customer);
        newRepairOrder.setInspectionReport(inspectionReport.get());
        newRepairOrder = repairOrderRepository.save(newRepairOrder);

        AddRepairOrderEvent event = new AddRepairOrderEvent(this, newRepairOrder.getId());
        applicationEventPublisher.publishEvent(event);

        log.info(String.format(
                "Reparatieorder met id: %s aangemaakt voor keuringsrapport-id : %s , auto-id : %s , klant-id %s ",
                newRepairOrder.getId(),
                inspectionReport.get().getId(),inspectionReport.get().getCar().getId()
                ,customer.getId()
        ));

    }

    public RepairOrder setStatus(Integer repairOrderId, RepairStatus status){
            RepairOrder currentRepairOrder = repairOrderRepository.getById(repairOrderId);
            currentRepairOrder.setStatus(status);

            events.notify(RepairStatus.VOLTOOID.toString());
            return repairOrderRepository.save(currentRepairOrder);
    }

    public EventManager getEvents() {
        return events;
    }

    @EventListener
    public void onInspectionStatusEvent(AddInspectionReportEvent event) {
       addRepairOrder(event.getCarId());
    }

    @EventListener
    public void handleApprovalStatusEvent(AddApprovalStatusEvent event){
        RepairOrder repairOrder = repairOrderRepository.getRepairOrderWithInspectionReportId(
                event.getInspectionReportId()
        );
        setStatus(repairOrder.getId(),RepairStatus.NIET_UITVOEREN);
    }

    @EventListener
    public void handleInspectionReportStatusEvent(ChangeInspectionStatusEvent event) {
        RepairOrder repairOrder = repairOrderRepository.getRepairOrderWithInspectionReportId(
                event.getInspectionReportId()
        );
        setStatus(repairOrder.getId(),RepairStatus.VOLTOOID);
    }
}
