package nl.jamienovi.garagemanagement.repairorderline;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.laboritem.Labor;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "RepairOrderLine")
@Table(name = "reparatie_bestelregel")
@Getter
@Setter
@NoArgsConstructor

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

//    @JsonIgnoreProperties("numberInStock")
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "part_code",nullable = true)
    @Column(name = "id_onderdeel")
    private String partId;

    @ManyToOne()
    @JoinColumn(name = "labor_id",nullable = true)
    private Labor labor;

    @Column(name = "prijs")
    private Double orderLinePrice;

    @Column(name = "aantal")
    private Integer orderLineQuantity;

}
