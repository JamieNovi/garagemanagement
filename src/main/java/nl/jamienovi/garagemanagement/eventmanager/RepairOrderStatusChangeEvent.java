package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RepairOrderStatusChangeEvent extends ApplicationEvent {
    private Integer inspectionReportId;

    public RepairOrderStatusChangeEvent(Object source, Integer inspectionReportId) {
        super(source);
        this.inspectionReportId = inspectionReportId;
    }
}
