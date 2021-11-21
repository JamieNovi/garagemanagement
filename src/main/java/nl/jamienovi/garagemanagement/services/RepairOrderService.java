package nl.jamienovi.garagemanagement.services;

import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderDto;
import nl.jamienovi.garagemanagement.repairorder.RepairStatus;

import java.util.List;

public interface RepairOrderService {
    List<RepairOrder> findAll();

    RepairOrder findOne(Integer repairOrderId);

    void add(Integer carId);

    RepairOrder saveAgreements(RepairOrderDto dto, Integer repairOrderId);

    RepairOrder setStatus(Integer repairOrderId, RepairStatus status);
}
