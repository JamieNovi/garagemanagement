package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionReportServiceImpl;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import nl.jamienovi.garagemanagement.invoice.InvoiceController;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderDto;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.repairorder.RepairStatus;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineServiceImpl;
import nl.jamienovi.garagemanagement.services.InvoiceService;
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
    private final RepairOrderLineServiceImpl repairOrderLineServiceImpl;
    private final InspectionReportServiceImpl inspectionReportServiceImpl;
    private final ShortComingRepository shortComingRepository;
    private final RepairOrderService repairOrderService;
    private final InvoiceService invoiceService;
    private final InvoiceController invoiceController;

    @Autowired
    public RepairOrderDataLoader(RepairOrderLineServiceImpl repairOrderLineServiceImpl,
                                 InspectionReportServiceImpl inspectionReportServiceImpl,
                                 ShortComingRepository shortComingRepository,
                                 RepairOrderService repairOrderService,
                                 InvoiceService invoiceService, InvoiceController invoiceController) {
        this.repairOrderLineServiceImpl = repairOrderLineServiceImpl;
        this.inspectionReportServiceImpl = inspectionReportServiceImpl;
        this.shortComingRepository = shortComingRepository;
        this.repairOrderService = repairOrderService;
        this.invoiceService = invoiceService;
        this.invoiceController = invoiceController;
    }


    @Override
    public void run(String... args) throws Exception {

        inspectionReportServiceImpl.addInspectionReport(1);
        InspectionReport inspectionReport = inspectionReportServiceImpl.getInspectionReport(1);

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

        inspectionReportServiceImpl.setInspectionReportStatus(1,InspectionStatus.AFGEKEURD);

        inspectionReportServiceImpl.setApprovalRepair(1,RepairApprovalStatus.AKKOORD);

        repairOrderService.saveAgreements(new RepairOrderDto(null,
                "Alles repareren",null
                ),1);

        repairOrderLineServiceImpl.addRepairOrderLaborItem(1,"H0000");

        log.info("Monteur is de de auto aan het repareren");

         /*
            Voeg onderdelen en handelingen toe aan bestel-regels aan klant met id klantid 1
         */
        log.info("Monteur heeft de reparatie voltooid en voegt onderdelen toe aan de reparatieorder");

        repairOrderLineServiceImpl.addRepairOrderPartItem(1,"P001",1);
        repairOrderLineServiceImpl.addRepairOrderLaborItem(1,"HP001");

        repairOrderLineServiceImpl.addRepairOrderPartItem(1, "P002",1);
        repairOrderLineServiceImpl.addRepairOrderLaborItem(1,"HP002");

        repairOrderLineServiceImpl.addRepairOrderPartItem(1, "P003",1);
        repairOrderLineServiceImpl.addRepairOrderLaborItem(1, "HP003");

        repairOrderLineServiceImpl.addRepairOrderPartItem(1,"P004", 1);
        repairOrderLineServiceImpl.addRepairOrderLaborItem(1, "HP004");

        repairOrderService.setStatus(1, RepairStatus.VOLTOOID);

//        /*
//        Auto van klant 2
//         */
//
        inspectionReportServiceImpl.addInspectionReport(2);

        repairOrderLineServiceImpl.addRepairOrderLaborItem(2,"H0000");
        inspectionReportServiceImpl.setInspectionReportStatus(2,InspectionStatus.GOEDGEKEURD);

        /*
        Auto van klant 3
         */

        inspectionReportServiceImpl.addInspectionReport(3);

        // Toevoegen keuringstarief
        repairOrderLineServiceImpl.addRepairOrderLaborItem(3,"H0000");

        //Monteur keurt auto af en zet dat in het rapport
        inspectionReportServiceImpl.setInspectionReportStatus(3,InspectionStatus.AFGEKEURD);

        //Monteur zet in het systeem dat klant niet akkoord gaat.
        //Reparatie wordt door event op NIET_UITVOEREN Gezet

        inspectionReportServiceImpl.setApprovalRepair(3, RepairApprovalStatus.NIETAKKOORD);
    }
}
