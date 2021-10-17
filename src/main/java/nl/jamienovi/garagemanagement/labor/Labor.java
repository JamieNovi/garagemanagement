package nl.jamienovi.garagemanagement.labor;

import lombok.*;

import javax.persistence.*;


@Entity(name = "Labor")
@Table(name = "handelingen")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Labor {

    @Id
    @Column(name = "handeling_id")
    private String id;

    @Column(name = "omschrijving_handeling")
    private String name;

    @Column(name = "prijs_handeling")
    private Double price;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ItemType type = ItemType.HANDELING;

    public Labor(String id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
