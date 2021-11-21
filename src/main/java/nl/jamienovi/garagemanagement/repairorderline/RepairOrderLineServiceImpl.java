package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.labor.ItemType;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborService;
import nl.jamienovi.garagemanagement.part.Item;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.services.RepairOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RepairOrderLineServiceImpl implements RepairOrderLineService {
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderService repairOrderService;
    private final PartService partService;
    private final LaborService laborService;

    @Autowired
    public RepairOrderLineServiceImpl(RepairOrderLineRepository repairOrderLineRepository,
                                      RepairOrderService repairOrderService, PartService partService,
                                      LaborService laborService) {
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderService = repairOrderService;
        this.partService = partService;
        this.laborService = laborService;
    }

    @Override
    public List<RepairOrderLine> getAll() {
        return repairOrderLineRepository.findAll();
    }


    @Override
    public void addRepairOrderPartItem(Integer repairOrderId, String partCode, Integer quantity){
        RepairOrder repairOrder = repairOrderService.findOne(repairOrderId);
        Part addedPart = partService.findOne(partCode);
        RepairOrderLine line = buildOrderline(repairOrder, addedPart);
        line.setOrderLinePrice(addedPart.getPrice() * quantity);
        repairOrderLineRepository.save(line);
        log.info("Onderdeel: {} toegevoegd aan reparatie-order: {}",addedPart.getName(),repairOrderId);
    }

    @Override
    public RepairOrderLine buildOrderline(RepairOrder repairOrder, Item item) {
        RepairOrderLine line = new RepairOrderLine();
        line.setRepairOrder(repairOrder);
        if(item.getType() == ItemType.ONDERDEEL){
            line.setPartId(item.getId());
            line.setLaborId(null);
        }else if (item.getType() == ItemType.HANDELING) {
            line.setLaborId(item.getId());
            line.setPartId(null);
        }
        line.setDescription(item.getName());
        line.setOrderLinePrice(item.getPrice());
        repairOrder.getRepairOrderLines().add(line);
        return line;
    }

    @Override
    public void addRepairOrderLaborItem(Integer repairOrderId, String laborId){
        RepairOrder repairOrder = repairOrderService.findOne(repairOrderId);
        Labor addedLabor = laborService.findOne(laborId);
        RepairOrderLine line =  buildOrderline(repairOrder,addedLabor);
        repairOrderLineRepository.save(line);
        log.info("Handeling: {} toegevoegd aan reparatie-order: {} ",addedLabor.getName(),repairOrderId);
    }
}
