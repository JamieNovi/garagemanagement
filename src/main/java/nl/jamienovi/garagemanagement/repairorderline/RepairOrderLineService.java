package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborService;
import nl.jamienovi.garagemanagement.part.Item;
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


    public void addRepairOrderPartItem(Integer repairOrderId, String partCode, Integer quantity){
        RepairOrder repairOrder = repairOrderService.getSingle(repairOrderId);

        Part addedPart = partService.getPart(partCode);

//        RepairOrderLine line = new RepairOrderLine();

//        line.setRepairOrder(repairOrder);
//
//        line.setPartId(addedPart.getId());
//
//        line.setOrderLinePrice(addedPart.getPrice() * quantity);
//
//        line.setOrderLineQuantity(quantity);
//
//        repairOrder.getRepairOrderLines().add(line);
//
//        repairOrderLineRepository.save(line);
        RepairOrderLine line = buildOrderline(repairOrder, addedPart);
        repairOrderLineRepository.save(line);

        log.info("Onderdeel: {} toegevoegd aan reparatie-order: {}",addedPart.getName(),repairOrderId);
    }

//    public RepairOrderLine buildLaborOrderline(RepairOrder repairOrder,Labor labor) {
//        RepairOrderLine line = new RepairOrderLine();
//        line.setRepairOrder(repairOrder);
//        line.setLaborId(labor.getId());
//        line.setOrderLinePrice(labor.getPrice());
//        repairOrder.getRepairOrderLines().add(line);
//        return line;
//    }

    public RepairOrderLine buildOrderline(RepairOrder repairOrder, Item item) {
        RepairOrderLine line = new RepairOrderLine();
        line.setRepairOrder(repairOrder);
        line.setLaborId(item.getId());
        line.setPartId(item.getId());
        line.setOrderLinePrice(item.getPrice());
        repairOrder.getRepairOrderLines().add(line);
        return line;
    }


    public void addRepairOrderLaborItem(Integer repairOrderId,String laborId){
        RepairOrder repairOrder = repairOrderService.getSingle(repairOrderId);
        Labor addedLabor = laborService.getSingle(laborId);

        RepairOrderLine line =  buildOrderline(repairOrder,addedLabor);
        repairOrderLineRepository.save(line);

        log.info("Handeling: {} toegevoegd aan reparatie-order: {} ",addedLabor.getName(),repairOrderId);
    }

}
