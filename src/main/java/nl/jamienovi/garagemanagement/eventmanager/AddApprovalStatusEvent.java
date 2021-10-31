package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddApprovalStatusEvent extends ApplicationEvent {
    private Integer inspectionReportId;
    private RepairApprovalStatus repairApprovalStatus;

    public AddApprovalStatusEvent(Object source,
                                  Integer inspectionReportId,
                                  RepairApprovalStatus repairApprovalStatus) {
        super(source);
        this.inspectionReportId = inspectionReportId;
        this.repairApprovalStatus = repairApprovalStatus;
    }
}
