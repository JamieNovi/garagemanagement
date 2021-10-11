package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairOrderService {
    private final RepairOrderRepository repairOrderRepository;

    @Autowired
    public RepairOrderService(RepairOrderRepository repairOrderRepository) {
        this.repairOrderRepository = repairOrderRepository;
    }

    public List<RepairOrder> getAll() {
        return repairOrderRepository.findAll();
    }

    public RepairOrder getSingle(Integer repairOrderId) {
        RepairOrder repairOrder = repairOrderRepository.findById(repairOrderId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(RepairOrder.class,"id", repairOrderId.toString());
                });
        return repairOrder;
    }

    public RepairOrder setStatus(Integer repairOrderId, RepairStatus status){
            RepairOrder currentRepairOrder = repairOrderRepository.getById(repairOrderId);
            currentRepairOrder.setStatus(status);
            return repairOrderRepository.save(currentRepairOrder);
    }

}
