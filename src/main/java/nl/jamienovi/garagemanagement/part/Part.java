package nl.jamienovi.garagemanagement.part;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.laboritem.ItemType;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CarPart")
@Table(name = "onderdelen")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Part {

    @Id
    @SequenceGenerator(name = "carpart_sequence",sequenceName = "carpart_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "carpart_sequence")
    @Column(name = "onderdeel_code")
    private Integer id;

    @Column(name = "onderdeel_naam")
    private String name;

    @Column(name = "onderdeel_prijs")
    private Double price;

    @Column(name = "item_type")
    @Enumerated(EnumType.STRING)
    private ItemType typeofItem = ItemType.PART_ITEM;

    @Column(name = "voorraad",nullable = true)
    private Integer numberInStock;

    public Part(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Part(String name, Double price, Integer numberInStock) {
        this.name = name;
        this.price = price;
        this.numberInStock = numberInStock;
    }

}
