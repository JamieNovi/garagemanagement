package nl.jamienovi.garagemanagement.repairorder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLine;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

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
    @Column(name = "id", updatable = false)
    private Integer id;

    @OneToOne(mappedBy = "repairOrder", cascade = CascadeType.MERGE)
    private InspectionReport inspectionReport;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "repair_status")
    @Enumerated(EnumType.STRING)
    private RepairStatus status = RepairStatus.NIEUW;

    @Column(name = "afspraken")
    private String agreementComments;

    @JsonIgnoreProperties("repairOrder")
    @OneToMany(mappedBy = "repairOrder")
    private List<RepairOrderLine> repairOrderLines;

    public RepairOrder(String agreementComments) {
        this.agreementComments = agreementComments;
    }
}
