package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class InspectionReportController {
    private final InspectionService inspectionService;

    @Autowired
    public InspectionReportController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @GetMapping(path ="/keuring")
    public List<InspectionReport> getAllInspectionReports(){
        return inspectionService.getAllInspectionReports();
    }

    @PostMapping(path = "/keuring-aanmaken/{carId}")
    public ResponseEntity<?> addInspectionReportToCar(@PathVariable("carId") int carId,@RequestBody InspectionReport newInspectionReport){
        inspectionService.addInspectionReportToCar(carId, newInspectionReport);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport toegevoegd."));
    }
}
