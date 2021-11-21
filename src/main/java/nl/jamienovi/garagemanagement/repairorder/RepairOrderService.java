package nl.jamienovi.garagemanagement.repairorder;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerServiceImpl;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.*;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RepairOrderService implements nl.jamienovi.garagemanagement.services.RepairOrderService {
    private final RepairOrderRepository repairOrderRepository;
    private final CustomerServiceImpl customerServiceImpl;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DtoMapper mapper;


    @Autowired
    public RepairOrderService(RepairOrderRepository repairOrderRepository,
                              CustomerServiceImpl customerServiceImpl,
                              ApplicationEventPublisher applicationEventPublisher, DtoMapper mapper) {
        this.repairOrderRepository = repairOrderRepository;
        this.customerServiceImpl = customerServiceImpl;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public List<RepairOrder> findAll() {
        return repairOrderRepository.findAll();
    }

    @Override
    public RepairOrder findOne(Integer repairOrderId) {
        RepairOrder repairOrder = repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() ->  new EntityNotFoundException(
                        RepairOrder.class,"id", repairOrderId.toString())
                );

        return repairOrder;
    }

    @Override
    public void add(Integer carId) {
        Optional<InspectionReport> inspectionReport = repairOrderRepository.getInspectionReportFromCustomer(carId);
        Customer customer = customerServiceImpl.findOne(inspectionReport.get().getCar().getCustomer().getId());

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

    @Override
    public RepairOrder saveAgreements(RepairOrderDto dto, Integer repairOrderId) {
        RepairOrder repairOrder= repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() ->new EntityNotFoundException(
                        RepairOrder.class,"id",repairOrderId.toString())
                );
        mapper.updateRepairOrderFromDto(dto,repairOrder);
        log.info("Afspraak toegevoegd aan reparatieorder: {}",dto.getAgreementComments());
        repairOrderRepository.save(repairOrder);
        return repairOrder;
    }

    @Override
    public RepairOrder setStatus(Integer repairOrderId, RepairStatus status){
            RepairOrder currentRepairOrder = repairOrderRepository.getById(repairOrderId);
            Integer inspectionReportId = currentRepairOrder.getInspectionReport().getId();

            currentRepairOrder.setStatus(status);
            switch(status){
                case BETAALD:
                    log.info("RepairOrder-id : {} STATUS {}",
                            repairOrderId,status);
                case VOLTOOID:
                    RepairOrderCompletedEvent event1 = new RepairOrderCompletedEvent(this,inspectionReportId);
                    applicationEventPublisher.publishEvent(event1);
                    log.info("Voltooid vanuit setStatus {}: ", inspectionReportId);

            }
            log.info("Reparatie-id: {} is klaar. Reparatieorder STATUS: {}. Klant auto klaar melden",
                repairOrderId,
                RepairStatus.VOLTOOID);
            return repairOrderRepository.save(currentRepairOrder);
    }

    @EventListener
    public void onAddInspectionStatusEvent(AddInspectionReportEvent event) {
       add(event.getCarId());
    }

    @EventListener
    public void handleApprovalStatusEvent(AddApprovalStatusEvent event){
        RepairOrder repairOrder = repairOrderRepository.getRepairOrderWithInspectionReportId(
                event.getInspectionReportId()
        );
        if(event.getRepairApprovalStatus() == RepairApprovalStatus.NIETAKKOORD){
            setStatus(repairOrder.getId(),RepairStatus.NIET_UITVOEREN);

            log.info("Reparatieorder-id: {} Nieuwe status: {}. Klant auto klaar melden.",
                    repairOrder.getId(),RepairStatus.NIET_UITVOEREN);

        }else if(event.getRepairApprovalStatus() == RepairApprovalStatus.AKKOORD){
            setStatus(repairOrder.getId(),RepairStatus.UITVOEREN);

            log.info("Reparatieorder-id: {} Nieuwe status: {}",
                    repairOrder.getId(),RepairStatus.UITVOEREN);
        }
    }

    @EventListener
    public void handleInspectionReportStatusEventGoedGekeurd(ChangeInspectionStatusEvent event) {
        RepairOrder repairOrder = repairOrderRepository.getRepairOrderWithInspectionReportId(
                event.getInspectionReportId()
        );
        setStatus(repairOrder.getId(),RepairStatus.NIET_UITVOEREN);
    }

}
