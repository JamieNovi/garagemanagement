package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.repairorder.RepairStatus;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import nl.jamienovi.garagemanagement.shortcoming.ShortComingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Slf4j
@Order(3)
@Transactional
public class RepairOrderDataLoader implements CommandLineRunner {
    private final RepairOrderLineService repairOrderLineService;
    private final InspectionService inspectionService;
    private final ShortComingRepository shortComingRepository;
    private final RepairOrderService repairOrderService;

    @Autowired
    public RepairOrderDataLoader(RepairOrderLineService repairOrderLineService,
                                 InspectionService inspectionService, ShortComingRepository shortComingRepository, RepairOrderService repairOrderService) {
        this.repairOrderLineService = repairOrderLineService;
        this.inspectionService = inspectionService;
        this.shortComingRepository = shortComingRepository;
        this.repairOrderService = repairOrderService;
    }


    @Override
    public void run(String... args) throws Exception {

        inspectionService.addInspectionReportToCar(1);
        InspectionReport inspectionReport = inspectionService.getSingleInspectionReport(1);

        ShortComing shortComing1 = new ShortComing("Remmen versleten");
        shortComing1.setInspectionReport(inspectionReport);

        ShortComing shortComing2 = new ShortComing("Lekkende uitlaat");
        shortComing2.setInspectionReport(inspectionReport);

        ShortComing shortComing3 = new ShortComing("Lekkende Oliefilter");
        shortComing3.setInspectionReport(inspectionReport);

        ShortComing shortComing4 = new ShortComing("Defecte bougies");
        shortComing4.setInspectionReport(inspectionReport);

        ShortComing shortComing5 = new ShortComing("Koplamp rechts stuk");
        shortComing5.setInspectionReport(inspectionReport);

        shortComingRepository.saveAll(List.of(shortComing1,shortComing2,shortComing3,shortComing4,
                shortComing5));

        inspectionReport.setStatus(InspectionStatus.AFGEKEURD);

        repairOrderService.setStatus(1, RepairStatus.UITVOEREN);

        repairOrderLineService.addRepairOrderLaborItem(1,"H0000");

         /*
            Voeg onderdelen en handelingen toe aan bestel-regels aan klant met id klantid 1
         */
           repairOrderLineService.addRepairOrderItem(1,"P001",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP002");

        repairOrderLineService.addRepairOrderItem(1, "P002",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP003");

        repairOrderLineService.addRepairOrderItem(1, "P003",1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP004");

        repairOrderLineService.addRepairOrderItem(1,"P004", 1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP005");

        /*
        Auto van klant 2
         */

        inspectionService.addInspectionReportToCar(2);
        repairOrderLineService.addRepairOrderLaborItem(2,"H0000");
        inspectionService.setInspectionReportStatus(2,InspectionStatus.GOEDGEKEURD);
        //repairOrderService.setStatus(2,RepairStatus.VOLTOOID);

        /*
        Auto van klant 3
         */

        inspectionService.addInspectionReportToCar(3);

        // Toevoegen keuringstarief
        repairOrderLineService.addRepairOrderLaborItem(3,"H0000");

        //Monteur keurt auto af en zet dat in het rapport
        inspectionService.setInspectionReportStatus(3,InspectionStatus.AFGEKEURD);

        //Monteur zet in het systeem dat klant niet akkoord gaat.
        //Reparatie wordt door event op NIET_UITVOEREN Gezet

        //inspectionService.setApprovalRepair(3, ApprovalStatus.NIETAKKOORD);
        //Factuur wordt automatisch opgeslagen door event vanuit repairorder


    }


}
