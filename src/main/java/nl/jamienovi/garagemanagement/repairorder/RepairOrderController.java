package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('repairorder:read','repairorder:write')")
    @GetMapping(path="")
    public List<RepairOrder> getAll(){
        return repairOrderService.findAll();
    }


    @PreAuthorize("hasAnyAuthority('repairorder:read','repairorder:write')")
    @GetMapping(path="/{repairId}")
    public ResponseEntity<?> getSingle(@PathVariable("repairId") Integer repairId){
        RepairOrder repairOrder = repairOrderService.findOne(repairId);
        if(repairOrder == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(repairOrder);
        }
    }
    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<?> create(@PathVariable Integer carId){
        repairOrderService.add(carId);
        return ResponseEntity.ok(new ResponseMessage("Reparatie toegevoegd."));
    }

    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PutMapping(path = "/{repairId}")
    public RepairOrder setRepairOrderStatus(@PathVariable()Integer repairId,
                                            @RequestParam("status") RepairStatus status) throws EntityNotFoundException {

        return repairOrderService.setStatus(repairId,status);
    }

    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PutMapping(path = "/afspraken/{repairId}")
    public ResponseEntity<RepairOrder> addRepairAgreements(@PathVariable Integer repairId,
                                                           @RequestBody RepairOrderDto repairOrderDto) {
        RepairOrder repairOrder = repairOrderService.saveAgreements(repairOrderDto,repairId);
        return ResponseEntity.ok().body(repairOrder);
    }
}
