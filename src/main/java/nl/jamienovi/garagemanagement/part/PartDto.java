package nl.jamienovi.garagemanagement.part;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.labor.ItemType;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PartDto {
    private String code;
    private String name;
    private Double price;
    private ItemType type;
    private Integer numberInStock;
}
