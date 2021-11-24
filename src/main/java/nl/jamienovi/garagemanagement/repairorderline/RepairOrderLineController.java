package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
public class RepairOrderLineController {
    private final RepairOrderLineServiceImpl repairOrderLineServiceImpl;

    @Autowired
    public RepairOrderLineController(RepairOrderLineServiceImpl repairOrderLineServiceImpl) {
        this.repairOrderLineServiceImpl = repairOrderLineServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('repairorderline:read','repairorderline:write')")
    @GetMapping(path = "/reparatie-regels")
    public List<RepairOrderLine> getAll() {
        return repairOrderLineServiceImpl.getAll();
    }

    @PreAuthorize("hasAnyAuthority('repairorderline:write')")
    @PostMapping(path = "/reparatie-onderdeel/{repairOrderId}")
    public ResponseEntity<ResponseMessage> addRepairOrderPartItem(@PathVariable("repairOrderId") Integer repairOrderId,
                                                 @RequestParam("id") String partId,
                                                 @RequestParam("quantity") Integer quantity){
        RepairOrderLine line = repairOrderLineServiceImpl.addRepairOrderPartItem(repairOrderId,partId, quantity);
        return ResponseEntity.ok(new ResponseMessage(
                "Onderdeel id:  " + line.getPartId() + ", " + line.getDescription() + " toegevoegd aan reparatie."));
    }

    @PreAuthorize("hasAnyAuthority('repairorderline:write')")
    @PostMapping(path = "/reparatie-handeling/{repairOrderId}")
    public ResponseEntity<ResponseMessage> addRepairOrderLaborItem(@PathVariable("repairOrderId") Integer repairOrderId,
                                        @RequestParam("id") String laborId){
        RepairOrderLine line = repairOrderLineServiceImpl.addRepairOrderLaborItem(repairOrderId,laborId);
        return ResponseEntity.ok(new ResponseMessage("Handeling id:  " + line.getLaborId() + ", " + line.getDescription() + " toegevoegd aan reparatie."));
    }
}
