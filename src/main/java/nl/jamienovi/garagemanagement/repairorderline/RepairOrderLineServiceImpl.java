package nl.jamienovi.garagemanagement.repairorderline;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.labor.ItemType;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborService;
import nl.jamienovi.garagemanagement.part.RepairItem;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderServiceImpl;
import nl.jamienovi.garagemanagement.interfaces.RepairOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RepairOrderLineServiceImpl implements RepairOrderLineService {
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderServiceImpl repairOrderServiceImpl;
    private final PartService partService;
    private final LaborService laborService;

    @Autowired
    public RepairOrderLineServiceImpl(RepairOrderLineRepository repairOrderLineRepository,
                                      RepairOrderServiceImpl repairOrderServiceImpl,
                                      PartService partService,
                                      LaborService laborService) {
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderServiceImpl = repairOrderServiceImpl;
        this.partService = partService;
        this.laborService = laborService;
    }

    @Override
    public List<RepairOrderLine> getAll() {
        return repairOrderLineRepository.findAll();
    }


    @Override
    public RepairOrderLine addRepairOrderPartItem(Integer repairOrderId, String partCode, Integer quantity){
        RepairOrder repairOrder = repairOrderServiceImpl.findOne(repairOrderId);
        Part addedPart = partService.findOne(partCode);
        RepairOrderLine line = buildOrderline(repairOrder, addedPart);
        line.setOrderLinePrice(addedPart.getPrice() * quantity);
        log.info("Onderdeel: {} toegevoegd aan reparatie-order: {}",addedPart.getName(),repairOrderId);
        return repairOrderLineRepository.save(line);
    }

    @Override
    public RepairOrderLine addRepairOrderLaborItem(Integer repairOrderId, String laborId){
        RepairOrder repairOrder = repairOrderServiceImpl.findOne(repairOrderId);
        Labor addedLabor = laborService.findOne(laborId);
        RepairOrderLine line =  buildOrderline(repairOrder,addedLabor);
        log.info("Handeling: {} toegevoegd aan reparatie-order: {} ",addedLabor.getName(),repairOrderId);
        return repairOrderLineRepository.save(line);
    }

    @Override
    public RepairOrderLine buildOrderline(RepairOrder repairOrder, RepairItem repairItem) {
        RepairOrderLine line = new RepairOrderLine();
        line.setRepairOrder(repairOrder);

        if(repairItem.getType() == ItemType.ONDERDEEL){
            line.setPartId(repairItem.getId());
            line.setLaborId(null);
        }else if (repairItem.getType() == ItemType.HANDELING) {
            line.setLaborId(repairItem.getId());
            line.setPartId(null);
        }

        line.setDescription(repairItem.getName());
        line.setOrderLinePrice(repairItem.getPrice());
        repairOrder.getRepairOrderLines().add(line);
        return line;
    }
}
