package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.laboritem.Labor;
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

    @PostMapping(path = "/onderdeel-toevoegen/{repairOrder}")
    public void addRepairOrderItem(@PathVariable("repairOrder") Integer repairOrderId,@RequestBody Part repairItem){
        repairOrderLineService.addRepairOrderItem(repairOrderId,repairItem);
    }
    @PostMapping(path = "/handeling-toevoegen/{repairOrder}")
    public void addRepairOrderLaborItem(@PathVariable("repairOrder") Integer repairOrderId,@RequestBody Labor laborItem){
        repairOrderLineService.addRepairOrderLaborItem(repairOrderId,laborItem);
    }


}
