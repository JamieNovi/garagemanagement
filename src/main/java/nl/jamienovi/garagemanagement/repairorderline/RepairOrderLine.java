package nl.jamienovi.garagemanagement.repairorderline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.carpart.CarPart;
import nl.jamienovi.garagemanagement.laboritem.Labor;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "RepairOrderLine")
@Table(name = "reparatie_bestelregel")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties("repairOrder")
public class RepairOrderLine {

    @Id
    @SequenceGenerator(name = "repair_order_line_sequence",sequenceName = "repair_order_line_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "repair_order_line_sequence")
    @Column(name = "id", updatable = false)
    private Integer id;

    @ManyToOne(optional = false,cascade = CascadeType.MERGE)
    @JoinColumn(name = "repair_order_id",nullable = false,updatable = false)
    private RepairOrder repairOrder;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "carpart_id",nullable = true)
    private CarPart carpart;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "labor_id",nullable = true)
    private Labor labor;

    @Column(name = "prijs")
    private Double orderLinePrice;

    @Column(name = "aantal")
    private Integer orderLineQuantity;

}
