package nl.jamienovi.garagemanagement.repairorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/reparatie-orders")
public class RepairOrderController {
    private final RepairOrderService repairOrderService;

    @Autowired
    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @GetMapping(path="")
    public List<RepairOrder> getAllRepairOrders(){
        return repairOrderService.getAll();
    }
}
