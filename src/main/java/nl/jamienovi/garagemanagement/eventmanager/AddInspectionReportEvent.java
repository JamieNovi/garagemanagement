package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddInspectionReportEvent extends ApplicationEvent {

    private Integer carId;


    public AddInspectionReportEvent(Object source, Integer carId) {
        super(source);
        this.carId = carId;
    }

}
