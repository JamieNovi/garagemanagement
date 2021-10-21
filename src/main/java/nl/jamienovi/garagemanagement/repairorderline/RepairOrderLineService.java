package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborService;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RepairOrderLineService {
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderService repairOrderService;
    private final PartService partService;
    private final LaborService laborService;

    @Autowired
    public RepairOrderLineService(RepairOrderLineRepository repairOrderLineRepository,
                                  RepairOrderService repairOrderService, PartService partService,
                                  LaborService laborService) {
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderService = repairOrderService;
        this.partService = partService;
        this.laborService = laborService;
    }

    public List<RepairOrderLine> getAll() {
        return repairOrderLineRepository.findAll();
    }


    public void addRepairOrderItem(Integer repairOrderId, String partCode, Integer quantity){
        RepairOrder repairOrder = repairOrderService.getSingle(repairOrderId);
        Part addedPart = partService.getPart(partCode);
        //Create new orderline
        RepairOrderLine line = new RepairOrderLine();
        //Set repairorder in orderline
        line.setRepairOrder(repairOrder);
        line.setPartId(addedPart.getId());
        //Set price quantity and repairItem of orderline
        line.setOrderLinePrice(addedPart.getPrice() * quantity);
        line.setOrderLineQuantity(quantity);
        //add orderlines to repairorder List<repairorderlines>
        repairOrder.getRepairOrderLines().add(line);
        repairOrderLineRepository.save(line);
    }

    public RepairOrderLine buildLaborOrderline(RepairOrder repairOrder,Labor labor) {
        RepairOrderLine line = new RepairOrderLine();
        line.setRepairOrder(repairOrder);
        line.setLaborId(labor.getId());
        line.setOrderLinePrice(labor.getPrice());
        repairOrder.getRepairOrderLines().add(line);
        return line;
    }


    public void addRepairOrderLaborItem(Integer repairOrderId,String laborId){
        RepairOrder repairOrder = repairOrderService.getSingle(repairOrderId);
        Labor addedLabor = laborService.getSingle(laborId);

        RepairOrderLine line =  buildLaborOrderline(repairOrder,addedLabor);
        repairOrderLineRepository.save(line);
    }


}
