package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/keuringen")
public class InspectionReportController {
    private final InspectionService inspectionService;

    @Autowired
    public InspectionReportController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @GetMapping(path ="")
    public List<InspectionReport> getAllInspectionReports(){
        return inspectionService.getAllInspectionReports();
    }

    @PostMapping(path = "/{carId}")
    public ResponseEntity<?> addInspectionReportToCar(@PathVariable("carId") Integer carId){
        inspectionService.addInspectionReportToCar(carId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport toegevoegd."));
    }

    @DeleteMapping(path = "/{inspectionReportId}")
    public ResponseEntity<?> deleteInspectionReport(@PathVariable Integer inspectionReportId) throws EntityNotFoundException{
        inspectionService.deleteInspectionReport(inspectionReportId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport verwijderd."));
    }

    @PutMapping(path = "/{inspectionReportId}")
    public ResponseEntity<?> setInspectionStatus(@PathVariable Integer inspectionReportId, @RequestParam("status") InspectionStatus status){
        inspectionService.setInspectionReportStatus(inspectionReportId, status );
        return ResponseEntity.ok(new ResponseMessage("Status keuringsrapport:" + status));
    }
}
