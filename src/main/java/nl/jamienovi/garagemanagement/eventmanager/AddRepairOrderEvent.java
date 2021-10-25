package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AddRepairOrderEvent extends ApplicationEvent {
    private Integer repairOrderId;

    public AddRepairOrderEvent(Object source,Integer repairOrderId) {
        super(source);
        this.repairOrderId = repairOrderId;
    }
}
