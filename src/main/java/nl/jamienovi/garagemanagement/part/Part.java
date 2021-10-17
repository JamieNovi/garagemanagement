package nl.jamienovi.garagemanagement.part;

import lombok.*;
import nl.jamienovi.garagemanagement.labor.ItemType;

import javax.persistence.*;

@Entity(name = "CarPart")
@Table(name = "onderdelen")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Part {
    @Id
    @Column(name = "id",unique = true)
    private String id;

    @Column(name = "onderdeel_naam")
    private String name;

    @Column(name = "onderdeel_prijs")
    private Double price;

    @Column(name = "reparatie_type")
    @Enumerated(EnumType.STRING)
    private ItemType type = ItemType.ONDERDEEL;

    @Column(name = "voorraad",nullable = true)
    private Integer numberInStock;

    public Part(String id, String name, Double price, Integer numberInStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numberInStock = numberInStock;
    }
}
