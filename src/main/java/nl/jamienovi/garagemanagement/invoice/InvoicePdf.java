package nl.jamienovi.garagemanagement.invoice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "InvoicePdf")
@Table(name = "facturen")
@NoArgsConstructor
@Getter
@Setter
public class InvoicePdf {

    @Id
    @Column(name = "klant_id")
    private Integer customerId;

    private String type = "pdf";

    @Lob
    private byte[] data;

    public InvoicePdf(Integer customerId, byte[] data) {
        this.customerId = customerId;
        this.data = data;
    }
}
