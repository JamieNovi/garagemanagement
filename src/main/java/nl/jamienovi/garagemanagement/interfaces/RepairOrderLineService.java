package nl.jamienovi.garagemanagement.interfaces;

import nl.jamienovi.garagemanagement.part.RepairItem;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLine;

import java.util.List;

public interface RepairOrderLineService {
    List<RepairOrderLine> getAll();

    RepairOrderLine addRepairOrderPartItem(Integer repairOrderId, String partCode, Integer quantity);

    RepairOrderLine buildOrderline(RepairOrder repairOrder, RepairItem repairItem);

    RepairOrderLine addRepairOrderLaborItem(Integer repairOrderId, String laborId);
}
