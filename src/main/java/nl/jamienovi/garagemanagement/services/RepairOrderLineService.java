package nl.jamienovi.garagemanagement.services;

import nl.jamienovi.garagemanagement.part.Item;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLine;

import java.util.List;

public interface RepairOrderLineService {
    List<RepairOrderLine> getAll();

    void addRepairOrderPartItem(Integer repairOrderId, String partCode, Integer quantity);

    RepairOrderLine buildOrderline(RepairOrder repairOrder, Item item);

    void addRepairOrderLaborItem(Integer repairOrderId, String laborId);
}
