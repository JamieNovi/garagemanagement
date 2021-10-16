package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairOrderService {
    private final RepairOrderRepository repairOrderRepository;
    private final CustomerService customerService;

    @Autowired
    public RepairOrderService(RepairOrderRepository repairOrderRepository,
                              CustomerService customerService) {
        this.repairOrderRepository = repairOrderRepository;
        this.customerService = customerService;
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
//        if(inspectionReport.isEmpty()){
//            throw new IllegalStateException("Auto is al in behandeling.");
//        }
        RepairOrder newRepairOrder = new RepairOrder(customer);
        newRepairOrder.setInspectionReport(inspectionReport.get());
        repairOrderRepository.save(newRepairOrder);

    }

    public RepairOrder setStatus(Integer repairOrderId, RepairStatus status){
            RepairOrder currentRepairOrder = repairOrderRepository.getById(repairOrderId);
            currentRepairOrder.setStatus(status);
            return repairOrderRepository.save(currentRepairOrder);
    }

}
