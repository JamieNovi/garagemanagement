package nl.jamienovi.garagemanagement.repairorderline;

import nl.jamienovi.garagemanagement.carpart.CarPart;
import nl.jamienovi.garagemanagement.laboritem.Labor;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairOrderLineService {
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderService repairOrderService;

    @Autowired
    public RepairOrderLineService(RepairOrderLineRepository repairOrderLineRepository,
                                  RepairOrderService repairOrderService) {
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderService = repairOrderService;
    }

    public List<RepairOrderLine> getAll() {
        return repairOrderLineRepository.findAll();
    }

    public void addRepairOrderItem(Integer repairOrderId, CarPart carpartItem){
        RepairOrder repairOrder = repairOrderService.getRepairOrder(repairOrderId);
        //Create new orderline
        RepairOrderLine line = new RepairOrderLine();
        //Set repairorder in orderline
        line.setRepairOrder(repairOrder);
        line.setCarpart(carpartItem);
        //Set price quantity and repairItem of orderline
        line.setOrderLinePrice(carpartItem.getPrice());
        line.setOrderLineQuantity(1);
        //add orderlines to repairorder List<repairorderlines>
        repairOrder.getRepairOrderLines().add(line);
        repairOrderLineRepository.save(line);
    }

    public void addRepairOrderLaborItem(Integer repairOrderId, Labor laborItem){
        RepairOrder repairOrder = repairOrderService.getRepairOrder(repairOrderId);
        //Create new orderline
        RepairOrderLine line = new RepairOrderLine();
        //Set repairorder in orderline
        line.setRepairOrder(repairOrder);
        line.setLabor(laborItem);
        //Set price quantity and repairItem of orderline
        line.setOrderLinePrice(laborItem.getPrice());
        line.setOrderLineQuantity(1);
        //add orderlines to repairorder List<repairorderlines>
        repairOrder.getRepairOrderLines().add(line);
        repairOrderLineRepository.save(line);
    }


}
