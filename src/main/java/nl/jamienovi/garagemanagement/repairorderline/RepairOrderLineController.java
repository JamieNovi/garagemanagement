package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class RepairOrderLineController {

    private final RepairOrderLineService repairOrderLineService;

    @Autowired
    public RepairOrderLineController(RepairOrderLineService repairOrderLineService) {
        this.repairOrderLineService = repairOrderLineService;
    }

    @GetMapping(path = "/reparatie-items")
    public List<RepairOrderLine> getAll() {
        return repairOrderLineService.getAll();
    }

    @PostMapping(path = "/onderdeel-toevoegen/{repairOrder}/{partId}")
    public void addRepairOrderItem(@PathVariable("repairOrder") Integer repairOrderId,
                                   @PathVariable("partId") String partId,
                                   @RequestParam("quantity") Integer quantity){
        repairOrderLineService.addRepairOrderItem(repairOrderId,partId, quantity);
    }
    @PostMapping(path = "/handeling-toevoegen/{repairOrder}/{laborId}")
    public void addRepairOrderLaborItem(@PathVariable("repairOrder") Integer repairOrderId,
                                        @PathVariable("laborId") String laborId){
        repairOrderLineService.addRepairOrderLaborItem(repairOrderId,laborId);
    }


}
