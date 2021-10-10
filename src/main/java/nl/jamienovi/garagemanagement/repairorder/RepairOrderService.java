package nl.jamienovi.garagemanagement.repairorder;

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

    public RepairOrder getRepairOrder(Integer repairOrderId) {
        return repairOrderRepository.getById(repairOrderId);
    }

    public void setRepairOrderStatus(Integer repairOrderId, RepairStatus status){

    }

}
