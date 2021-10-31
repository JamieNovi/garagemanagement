package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RepairOrderCompletedEvent extends ApplicationEvent {
    private Integer inspectionReportId;

    public RepairOrderCompletedEvent(Object source,Integer inspectionReportId) {
        super(source);
        this.inspectionReportId = inspectionReportId;
    }
}
