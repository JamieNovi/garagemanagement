package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddApprovalStatusEvent extends ApplicationEvent {
    private Integer inspectionReportId;

    public AddApprovalStatusEvent(Object source, Integer inspectionReportId) {
        super(source);
        this.inspectionReportId = inspectionReportId;
    }
}
