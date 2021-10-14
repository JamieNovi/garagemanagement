package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.laboritem.Labor;
import nl.jamienovi.garagemanagement.laboritem.LaborRepository;
import nl.jamienovi.garagemanagement.part.PartRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@Order(2)
@Transactional
public class RepairOrderDataLoader implements CommandLineRunner {
    private final LaborRepository laborRepository;
    private final RepairOrderLineService repairOrderLineService;
    private final PartRepository partRepository;

    public RepairOrderDataLoader(LaborRepository laborRepository,
                                 RepairOrderLineService repairOrderLineService,
                                 PartRepository partRepository) {
        this.laborRepository = laborRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.partRepository = partRepository;
    }

    @Override
    public void run(String... args) throws Exception {
          /*
            Voeg keuringstarief regel toe aan de reparatie-regels van reparatieorder(id=1)
            van klant(id=1) Tom Cruise
         */
//        Labor inspectionRateItem = laborRepository.getById(1);
//        log.info(inspectionRateItem.toString());
//        repairOrderLineService.addRepairOrderLaborItem(1,inspectionRateItem);
//        repairOrderLineService.addRepairOrderLaborItem(2,inspectionRateItem);
//
//         /*
//            Voeg onderdelen en handelingen toe aan bestel-regels
//         */
//           repairOrderLineService.addRepairOrderItem(1,"P001",1);
//        repairOrderLineService.addRepairOrderLaborItem(1,laborRepository.getById(2));

//        repairOrderLineService.addRepairOrderItem(1, partRepository.getById("P002"));
//        repairOrderLineService.addRepairOrderLaborItem(1,laborRepository.getById(3));
//
//        repairOrderLineService.addRepairOrderItem(1, partRepository.getById("P003"));
//        repairOrderLineService.addRepairOrderLaborItem(1, laborRepository.getById(3));
//
//        repairOrderLineService.addRepairOrderItem(1, partRepository.getById("P004"));
//        repairOrderLineService.addRepairOrderLaborItem(1, laborRepository.getById(4));
//
//        repairOrderLineService.addRepairOrderItem(2, partRepository.getById("P005"));
    }
}
