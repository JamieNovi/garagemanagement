package nl.jamienovi.garagemanagement.inspection;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.payload.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/keuringen")
public class InspectionReportController {
    private final InspectionReportServiceImpl inspectionReportServiceImpl;

    @Autowired
    public InspectionReportController(InspectionReportServiceImpl inspectionReportServiceImpl) {
        this.inspectionReportServiceImpl = inspectionReportServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('inspection:read','inspection:write')")
    @GetMapping(path ="")
    public List<InspectionReport> getAllInspectionReports(){
        return inspectionReportServiceImpl.getAllInspectionReports();
    }

    @PreAuthorize("hasAnyAuthority('inspection:read')")
    @PostMapping(path = "/{carId}")
    public ResponseEntity<ResponseMessage> addInspectionReport(@PathVariable("carId") Integer carId){
        inspectionReportServiceImpl.addInspectionReport(carId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport toegevoegd en reparatieorder toegevoegd."));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @DeleteMapping(path = "/{inspectionReportId}")
    public ResponseEntity<ResponseMessage> deleteInspectionReport(@PathVariable Integer inspectionReportId) throws CustomerEntityNotFoundException {
        inspectionReportServiceImpl.deleteInspectionReport(inspectionReportId);
        return ResponseEntity.ok(new ResponseMessage("Keuringsrapport verwijderd."));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @PutMapping(path = "/{inspectionReportId}")
    public ResponseEntity<ResponseMessage> setInspectionStatus(@PathVariable Integer inspectionReportId, @RequestParam("status") InspectionStatus status){
        inspectionReportServiceImpl.setInspectionReportStatus(inspectionReportId, status );
        return ResponseEntity.ok(new ResponseMessage("Status keuringsrapport:" + status));
    }

    @PreAuthorize("hasAnyAuthority('inspection:write')")
    @PutMapping(path = "/repareren/{inspectionReportId}")
    public ResponseEntity<ResponseMessage> setApprovalRepair(@PathVariable Integer inspectionReportId,
                                               @RequestParam("akkoord") RepairApprovalStatus status){
        inspectionReportServiceImpl.setApprovalRepair(inspectionReportId,status);
        return ResponseEntity.ok(new ResponseMessage("akkoord niet akkoord in systeem gezet"));
    }

}
