package nl.jamienovi.garagemanagement.repairorder;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/reparaties")
public class RepairOrderController {
    private final RepairOrderServiceImpl repairOrderServiceImpl;

    @Autowired
    public RepairOrderController(RepairOrderServiceImpl repairOrderServiceImpl) {
        this.repairOrderServiceImpl = repairOrderServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('repairorder:read','repairorder:write')")
    @GetMapping(path="")
    public List<RepairOrder> getAll(){
        return repairOrderServiceImpl.findAll();
    }


    @PreAuthorize("hasAnyAuthority('repairorder:read','repairorder:write')")
    @GetMapping(path="/{repairId}")
    public ResponseEntity<RepairOrder> getSingle(@PathVariable("repairId") Integer repairId){
        RepairOrder repairOrder = repairOrderServiceImpl.findOne(repairId);
        if(repairOrder == null){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(repairOrder);
        }
    }
    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<ResponseMessage> create(@PathVariable Integer carId){
        repairOrderServiceImpl.add(carId);
        return ResponseEntity.ok(new ResponseMessage("Reparatie toegevoegd."));
    }

    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PutMapping(path = "/{repairId}")
    public ResponseEntity<ResponseMessage> setRepairOrderStatus(@PathVariable()Integer repairId,
                                            @RequestParam("status") RepairStatus status) throws CustomerEntityNotFoundException {

        repairOrderServiceImpl.setStatus(repairId,status);
        return ResponseEntity.ok(new ResponseMessage("Status aangepast naar: " + status));
    }

    @PreAuthorize("hasAnyAuthority('repairorder:write')")
    @PutMapping(path = "/afspraken/{repairId}")
    public ResponseEntity<RepairOrder> addRepairAgreements(@PathVariable Integer repairId,
                                                           @RequestBody RepairOrderDto repairOrderDto) {
        RepairOrder repairOrder = repairOrderServiceImpl.saveAgreements(repairOrderDto,repairId);
        return ResponseEntity.ok().body(repairOrder);
    }
}
