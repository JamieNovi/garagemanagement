package nl.jamienovi.garagemanagement.repairorder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLine;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@CrossOrigin()
@Entity(name = "RepairOrder")
@Table(name = "repair_orders")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties("inspectionReport")
public class RepairOrder {
    @Id
    @SequenceGenerator(name="inspection_order_sequence", sequenceName = "repair_order_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "repair_order_sequence")
    @Column(name = "reparatie_id", updatable = false)
    private Integer id;

    @OneToOne(mappedBy = "repairOrder", cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    private InspectionReport inspectionReport;

    @Column(name = "aangemaakt_op")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RepairStatus status = RepairStatus.NIEUW;

    @Column(name = "afspraken")
    private String agreementComments = "Geen afspraken";

    @OneToMany(mappedBy = "repairOrder",orphanRemoval = true)
    private List<RepairOrderLine> repairOrderLines;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "klant_id",referencedColumnName = "id")
    private Customer customer;

    public RepairOrder(Customer customer) {
        this.customer = customer;
    }
}
