package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/keuringen")
public class InspectionReportController {
    private final InspectionReportService inspectionReportService;

    @Autowired
    public InspectionReportController(InspectionReportService inspectionReportService) {
        this.inspectionReportService = inspectionReportService;
    }

    @PreAuthorize("hasAnyAuthority('inspection:read','inspection:write')")
    @GetMapping(path ="")
    public List<InspectionReport> getAllInspectionReports(){
        return inspectionReportService.getAllInspectionReports();
    }

    @PreAuthorize("hasAnyAuthority('inspection:read')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<?> addInspectionReport(@PathVariable("carId") Integer carId){
        inspectionReportService.addInspectionReport(carId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport toegevoegd."));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @DeleteMapping(path = "/{inspectionReportId}")
    public ResponseEntity<?> deleteInspectionReport(@PathVariable Integer inspectionReportId) throws EntityNotFoundException{
        inspectionReportService.deleteInspectionReport(inspectionReportId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport verwijderd."));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @PutMapping(path = "/{inspectionReportId}")
    public ResponseEntity<?> setInspectionStatus(@PathVariable Integer inspectionReportId, @RequestParam("status") InspectionStatus status){
        inspectionReportService.setInspectionReportStatus(inspectionReportId, status );
        return ResponseEntity.ok(new ResponseMessage("Status keuringsrapport:" + status));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @PutMapping(path = "/repareren/{inspectionReportId}")
    public ResponseEntity<?> setApprovalRepair(@PathVariable Integer inspectionReportId,
                                               @RequestParam("akkoord") RepairApprovalStatus status){
        inspectionReportService.setApprovalRepair(inspectionReportId,status);
        return ResponseEntity.ok(new ResponseMessage("akkoord niet akkoord in systeem gezet"));
    }

}
