package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeInspectionStatusEvent extends ApplicationEvent {
    private Integer inspectionReportId;
    public ChangeInspectionStatusEvent(Object source,Integer inspectionReportId) {
        super(source);
        this.inspectionReportId = inspectionReportId;
    }
}
