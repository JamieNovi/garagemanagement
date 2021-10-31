package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChangeCarStatusEvent extends ApplicationEvent {
    private final Integer carId;

    public ChangeCarStatusEvent(Object source, Integer carId) {
        super(source);
        this.carId = carId;
    }
}
