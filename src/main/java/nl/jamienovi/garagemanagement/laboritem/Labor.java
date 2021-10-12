package nl.jamienovi.garagemanagement.laboritem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Labor")
@Table(name = "handelingen")
@Getter
@Setter
@NoArgsConstructor
public class Labor {

    @Id
    @SequenceGenerator(name = "labor_sequence",sequenceName = "labor_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "labor_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "Naam")
    private String name;

    @Column(name = "prijs")
    private Double price;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItemType type = ItemType.LABOR_ITEM;

    public Labor(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Labor(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
