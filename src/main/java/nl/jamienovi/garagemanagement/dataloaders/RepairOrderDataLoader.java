package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.labor.LaborRepository;
import nl.jamienovi.garagemanagement.part.PartRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final InspectionService inspectionService;
    private final CustomerService customerService;

    @Autowired
    public RepairOrderDataLoader(LaborRepository laborRepository,
                                 RepairOrderLineService repairOrderLineService,
                                 PartRepository partRepository, InspectionService inspectionService, CustomerService customerService) {
        this.laborRepository = laborRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.partRepository = partRepository;
        this.inspectionService = inspectionService;
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) throws Exception {

        inspectionService.addInspectionReportToCar(1);
        inspectionService.addInspectionReportToCar(2);
          /*
            Voeg keuringstarief regel toe aan de reparatie-regels van reparatieorder(id=1)
            van klant(id=1) Tom Cruise
         */
//        Labor inspectionRateItem = laborRepository.getById("H0000");
//        log.info(inspectionRateItem.toString());
//        repairOrderLineService.addRepairOrderLaborItem(1,inspectionRateItem.getId());
//        repairOrderLineService.addRepairOrderLaborItem(2,inspectionRateItem.getId());


         /*
            Voeg onderdelen en handelingen toe aan bestel-regels
         */
           repairOrderLineService.addRepairOrderItem(1,"P001",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP002");

        repairOrderLineService.addRepairOrderItem(1, "P002",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP003");

        repairOrderLineService.addRepairOrderItem(1, "P003",1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP004");

        repairOrderLineService.addRepairOrderItem(1,"P004", 1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP005");

        repairOrderLineService.addRepairOrderLaborItem(2, "H0000");
    }
}
