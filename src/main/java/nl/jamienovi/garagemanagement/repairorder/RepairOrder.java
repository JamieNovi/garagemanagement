package nl.jamienovi.garagemanagement.repairorder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "repair_orders")
public class RepairOrder {
    @Id
    private int id;
}
