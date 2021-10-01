package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/inspecties")
public class InspectionReportController {
    private final InspectionService inspectionService;

    public InspectionReportController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @GetMapping(path ="/")
    public List<InspectionReport> getAllInspectionReports(){
        return inspectionService.getAllInspectionReports();
    }

    @PostMapping(path = "/aanmaken/{carId}")
    public ResponseEntity<?> addInspectionReportToCar(@PathVariable("carId") int carId,@RequestBody InspectionReport newInspectionReport){
        inspectionService.addInspectionReportToCar(carId, newInspectionReport);
        return ResponseEntity.ok(new MessageResponse("Keuringsrapport toegevoegd."));
    }
}
