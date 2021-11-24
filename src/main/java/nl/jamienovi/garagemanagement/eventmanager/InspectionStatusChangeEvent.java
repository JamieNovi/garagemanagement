package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InspectionStatusChangeEvent extends ApplicationEvent {
    private Integer inspectionReportId;
    public InspectionStatusChangeEvent(Object source, Integer inspectionReportId) {
        super(source);
        this.inspectionReportId = inspectionReportId;
    }
}
