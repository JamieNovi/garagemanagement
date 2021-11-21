package nl.jamienovi.garagemanagement.services;

import nl.jamienovi.garagemanagement.eventmanager.RepairOrderCompletedEvent;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import org.springframework.context.event.EventListener;

import java.util.List;

public interface InspectionReportService {
    List<InspectionReport> getAllInspectionReports();

    InspectionReport getInspectionReport(Integer inspectionReportId);

    void addInspectionReport(Integer carId);

    void deleteInspectionReport(Integer inspectionReportId);

    void setInspectionReportStatus(Integer inspectionReportId, InspectionStatus status);

    void setIsRepaired(Integer inspectionReportId);

    void setApprovalRepair(Integer inspectionReportId, RepairApprovalStatus status);

    Boolean hasPendingStatus(Integer carId);

    @EventListener
    void handleRepairOrderStatusEvent(RepairOrderCompletedEvent event);
}
