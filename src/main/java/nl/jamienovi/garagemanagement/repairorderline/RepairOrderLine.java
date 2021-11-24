package nl.jamienovi.garagemanagement.repairorderline;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "RepairOrderLine")
@Table(name = "reparatie_bestelregels")
@Getter
@Setter
@ToString
public class RepairOrderLine {
    @Id
    @SequenceGenerator(name = "repair_order_line_sequence",sequenceName = "repair_order_line_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "repair_order_line_sequence")
    @Column(name = "id", updatable = false)
    private Integer id;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(optional = false,cascade = CascadeType.MERGE)
    @JoinColumn(name = "reparatie_id",nullable = false,referencedColumnName = "reparatie_id")
    private RepairOrder repairOrder;

    @Column(name = "id_onderdeel")
    private String partId;

    @Column(name = "id_handeling")
    private String laborId;

    @Column(name = "prijs")
    private Double orderLinePrice;

    @Column(name = "aantal")
    private Integer orderLineQuantity;

    @Column(name = "omschrijving")
    private String description;

    public RepairOrderLine() {
        if(orderLineQuantity == null) {
            orderLineQuantity = 1;
        }
    }

}
