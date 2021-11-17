package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionReportService;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import nl.jamienovi.garagemanagement.invoice.InvoiceController;
import nl.jamienovi.garagemanagement.invoice.InvoiceService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderDto;
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
    private final InspectionReportService inspectionReportService;
    private final ShortComingRepository shortComingRepository;
    private final RepairOrderService repairOrderService;
    private final InvoiceService invoiceService;
    private final InvoiceController invoiceController;

    @Autowired
    public RepairOrderDataLoader(RepairOrderLineService repairOrderLineService,
                                 InspectionReportService inspectionReportService,
                                 ShortComingRepository shortComingRepository,
                                 RepairOrderService repairOrderService,
                                 InvoiceService invoiceService, InvoiceController invoiceController) {
        this.repairOrderLineService = repairOrderLineService;
        this.inspectionReportService = inspectionReportService;
        this.shortComingRepository = shortComingRepository;
        this.repairOrderService = repairOrderService;
        this.invoiceService = invoiceService;
        this.invoiceController = invoiceController;
    }


    @Override
    public void run(String... args) throws Exception {

        inspectionReportService.addInspectionReport(1);
        InspectionReport inspectionReport = inspectionReportService.getInspectionReport(1);

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

        inspectionReportService.setInspectionReportStatus(1,InspectionStatus.AFGEKEURD);

        inspectionReportService.setApprovalRepair(1,RepairApprovalStatus.AKKOORD);

        repairOrderService.saveAgreements(new RepairOrderDto(null,
                "Alles repareren",null
                ),1);

        repairOrderLineService.addRepairOrderLaborItem(1,"H0000");

        log.info("Monteur is de de auto aan het repareren");

         /*
            Voeg onderdelen en handelingen toe aan bestel-regels aan klant met id klantid 1
         */
        log.info("Monteur heeft de reparatie voltooid en voegt onderdelen toe aan de reparatieorder");

        repairOrderLineService.addRepairOrderPartItem(1,"P001",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP001");

        repairOrderLineService.addRepairOrderPartItem(1, "P002",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP002");

        repairOrderLineService.addRepairOrderPartItem(1, "P003",1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP003");

        repairOrderLineService.addRepairOrderPartItem(1,"P004", 1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP004");

        repairOrderService.setStatus(1, RepairStatus.VOLTOOID);

//        /*
//        Auto van klant 2
//         */
//
        inspectionReportService.addInspectionReport(3);

        repairOrderLineService.addRepairOrderLaborItem(2,"H0000");
        inspectionReportService.setInspectionReportStatus(2,InspectionStatus.GOEDGEKEURD);

        /*
        Auto van klant 3
         */

        inspectionReportService.addInspectionReport(4);
//        inspectionService.addInspectionReportToCar(5);

        // Toevoegen keuringstarief
        repairOrderLineService.addRepairOrderLaborItem(3,"H0000");

        //Monteur keurt auto af en zet dat in het rapport
        inspectionReportService.setInspectionReportStatus(3,InspectionStatus.AFGEKEURD);

        //Monteur zet in het systeem dat klant niet akkoord gaat.
        //Reparatie wordt door event op NIET_UITVOEREN Gezet

        inspectionReportService.setApprovalRepair(3, RepairApprovalStatus.NIETAKKOORD);
        //Factuur wordt automatisch opgeslagen door event vanuit repairorder


    }


}
