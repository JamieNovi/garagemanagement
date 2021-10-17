package nl.jamienovi.garagemanagement.labor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaborDto {
    private String id;
    private String name;
    private Double price;
    private ItemType type;
}
