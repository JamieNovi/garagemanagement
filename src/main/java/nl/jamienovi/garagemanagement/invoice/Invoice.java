package nl.jamienovi.garagemanagement.invoice;

import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Invoice")
@Table(name = "Factuur")
@NoArgsConstructor
public class Invoice {
    @Id
    @SequenceGenerator(name = "invoice_sequence",sequenceName = "invoice_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "invoice_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "klant_id")
    private Integer customerId;

    @Column(name = "reparatie_id")
    private Integer repairOrderId;

    public Invoice(Integer customerId, Integer repairOrderId) {
        this.customerId = customerId;
        this.repairOrderId = repairOrderId;
    }
}
