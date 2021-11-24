package nl.jamienovi.garagemanagement.repairorder;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.eventmanager.ApprovalStatusChangeEvent;
import nl.jamienovi.garagemanagement.eventmanager.InspectionReportCreatedEvent;
import nl.jamienovi.garagemanagement.eventmanager.InspectionStatusChangeEvent;
import nl.jamienovi.garagemanagement.eventmanager.RepairOrderStatusChangeEvent;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import nl.jamienovi.garagemanagement.interfaces.CustomerService;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RepairOrderServiceImpl implements nl.jamienovi.garagemanagement.interfaces.RepairOrderService {
    private final RepairOrderRepository repairOrderRepository;
    private final CustomerService customerService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DtoMapper mapper;


    @Autowired
    public RepairOrderServiceImpl(RepairOrderRepository repairOrderRepository,
                                  CustomerService customerService,
                                  ApplicationEventPublisher applicationEventPublisher, DtoMapper mapper) {
        this.repairOrderRepository = repairOrderRepository;
        this.customerService = customerService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.mapper = mapper;
    }

    @Override
    public List<RepairOrder> findAll() {
        return repairOrderRepository.findAll();
    }

    @Override
    public RepairOrder findOne(Integer repairOrderId) {
        Optional<RepairOrder> repairOrderOptional = repairOrderRepository.findById(repairOrderId);
        if(repairOrderOptional.isEmpty()) {
            throw new CustomerEntityNotFoundException(
                    RepairOrder.class,"id", repairOrderId.toString()
            );
        }else {
            return repairOrderOptional.get();
        }
}

    @Override
    public void add(Integer carId) {
        Optional<InspectionReport> inspectionReport =
                repairOrderRepository.getInspectionReportByCarId(carId);
        if(inspectionReport.isEmpty()){
            throw new CustomerEntityNotFoundException(Car.class,"id",carId.toString());
        }
        Customer customer = customerService.findOne(inspectionReport.get().getCar().getCustomer().getId());

        RepairOrder newRepairOrder = new RepairOrder(customer);
        newRepairOrder.setInspectionReport(inspectionReport.get());
        newRepairOrder = repairOrderRepository.save(newRepairOrder);

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
                .orElseThrow(() -> new CustomerEntityNotFoundException(
                        RepairOrder.class,"id",repairOrderId.toString())
                );
        mapper.updateRepairOrderFromDto(dto,repairOrder);
        log.info("Afspraak toegevoegd aan reparatieorder: {}",dto.getAgreementComments());
        repairOrderRepository.save(repairOrder);
        return repairOrder;
    }

    @Override
    public RepairOrder setStatus(Integer repairOrderId, RepairStatus status){
            RepairOrder currentRepairOrder = repairOrderRepository.findById(repairOrderId)
                    .orElseThrow(
                            () -> new CustomerEntityNotFoundException(RepairOrder.class,"id",repairOrderId.toString())
                    );

            Integer inspectionReportId = currentRepairOrder.getInspectionReport().getId();

            currentRepairOrder.setStatus(status);
            if(status == RepairStatus.BETAALD) {
                log.info("RepairOrder-id : {} STATUS {}",
                        repairOrderId,status);
            }else if(status == RepairStatus.VOLTOOID) {
                RepairOrderStatusChangeEvent event1 = new RepairOrderStatusChangeEvent(this,inspectionReportId);
                applicationEventPublisher.publishEvent(event1);
                log.info("Voltooid vanuit setStatus {}: ", inspectionReportId);
            }
            log.info("Reparatie-id: {} is klaar. Reparatieorder STATUS: {}. Klant auto klaar melden",
                repairOrderId,
                RepairStatus.VOLTOOID);
            return repairOrderRepository.save(currentRepairOrder);
    }

    @EventListener
    public void onAddInspectionStatusEvent(InspectionReportCreatedEvent event) {
       add(event.getCarId());
    }

    @EventListener
    public void handleApprovalStatusEvent(ApprovalStatusChangeEvent event){
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
    public void handleInspectionReportStatusEventGoedGekeurd(InspectionStatusChangeEvent event) {
        RepairOrder repairOrder = repairOrderRepository.getRepairOrderWithInspectionReportId(
                event.getInspectionReportId()
        );
        setStatus(repairOrder.getId(),RepairStatus.NIET_UITVOEREN);
    }

}
