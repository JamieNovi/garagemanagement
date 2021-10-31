package nl.jamienovi.garagemanagement.eventmanager;

import lombok.Getter;
import nl.jamienovi.garagemanagement.invoice.InvoiceStatus;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvoicePaidEvent extends ApplicationEvent {
    private InvoiceStatus status;

    public InvoicePaidEvent(Object source, InvoiceStatus status) {
        super(source);
        this.status = status;
    }
}
