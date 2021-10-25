package nl.jamienovi.garagemanagement.repairorderline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RepairOrderLineDto {
    private Integer id;
    private Integer klant_id;
    private String name;
    private Double price;
    private Integer orderLineQuantity;

}
