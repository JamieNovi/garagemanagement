package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('repairorderline:read','repairorderline:write')")
    @GetMapping(path = "/reparatie-regels")
    public List<RepairOrderLine> getAll() {
        return repairOrderLineService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('repairorderline:write')")
    @PostMapping(path = "/reparatie-onderdeel/{repairOrderId}/{partId}")
    public void addRepairOrderPartItem(@PathVariable("repairOrderId") Integer repairOrderId,
                                       @PathVariable("partId") String partId,
                                       @RequestParam("quantity") Integer quantity){
        repairOrderLineService.addRepairOrderPartItem(repairOrderId,partId, quantity);
    }

    @PreAuthorize("hasAnyAuthority('repairorderline:write')")
    @PostMapping(path = "/reparatie-handeling/{repairOrder}/{laborId}")
    public void addRepairOrderLaborItem(@PathVariable("repairOrder") Integer repairOrderId,
                                        @PathVariable("laborId") String laborId){
        repairOrderLineService.addRepairOrderLaborItem(repairOrderId,laborId);
    }


}
