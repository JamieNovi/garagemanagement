package nl.jamienovi.garagemanagement.repairorder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RepairOrderDto{
    private Integer id;
    private String agreementComments;
    private RepairStatus repairStatus;
}
