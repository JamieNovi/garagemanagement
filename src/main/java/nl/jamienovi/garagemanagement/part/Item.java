package nl.jamienovi.garagemanagement.part;

import lombok.Getter;
import lombok.Setter;
import nl.jamienovi.garagemanagement.labor.ItemType;

@Getter
@Setter
public abstract class Item {
    private String id;
    private String name;
    private Double price;
    private ItemType type;

}
