package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InspectionReportCreatedEvent extends ApplicationEvent {

    private Integer carId;


    public InspectionReportCreatedEvent(Object source, Integer carId) {
        super(source);
        this.carId = carId;
    }

}
