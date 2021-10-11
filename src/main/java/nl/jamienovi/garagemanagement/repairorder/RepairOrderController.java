package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/reparaties")
public class RepairOrderController {
    private final RepairOrderService repairOrderService;

    @Autowired
    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @GetMapping(path="")
    public List<RepairOrder> getAll(){
        return repairOrderService.getAll();
    }

    @GetMapping(path="/{repairId}")
    public ResponseEntity<?> getSingle(@PathVariable("repairId") Integer repairId){
        RepairOrder repairOrder = repairOrderService.getSingle(repairId);
        if(repairOrder == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(repairOrder);
        }

    }

    @PutMapping(path = "/{repairId}")
    public RepairOrder setRepairOrderStatus(@PathVariable()Integer repairId, @RequestParam("status") RepairStatus status) throws EntityNotFoundException {
        return repairOrderService.setStatus(repairId,status);
    }
}
