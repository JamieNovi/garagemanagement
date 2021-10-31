package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.inspection.RepairApprovalStatus;
import nl.jamienovi.garagemanagement.invoice.InvoiceService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderDto;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.repairorder.RepairStatus;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import nl.jamienovi.garagemanagement.shortcoming.ShortComingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

@Slf4j
//@Component
//@Order(2)
//@Transactional
public class FlowAfgekeurdWelRepareren implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;
    private final InspectionService inspectionService;
    private final ShortComingRepository shortComingRepository;
    private final RepairOrderLineService repairOrderLineService;
    private final RepairOrderService repairOrderService;

    @Autowired
    public FlowAfgekeurdWelRepareren(CustomerRepository customerRepository, InvoiceService invoiceService,
                                     InspectionService inspectionService,
                                     ShortComingRepository shortComingRepository,
                                     RepairOrderLineService repairOrderLineService,
                                     RepairOrderService repairOrderService) {
        this.customerRepository = customerRepository;
        this.invoiceService = invoiceService;
        this.inspectionService = inspectionService;
        this.shortComingRepository = shortComingRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.repairOrderService = repairOrderService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Klant toevoegen met twee auto's
                Car car1 = new Car("Ferrari","California","FJ-XZ-88");
        Car car4 = new Car("Audi","R8","87-23-WE");

        Customer customer1 = new Customer(
                "Tom",
                "Cruise",
                "tomcruise@hollywood.com",
                "Hollywood boulevard 131",
                "90024", "Los Angeles"
        );
        customer1.addCar(car1);
        customer1.addCar(car4);
        customer1 = customerRepository.save(customer1);


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

        inspectionService.setInspectionReportStatus(1, InspectionStatus.AFGEKEURD);

        inspectionService.setApprovalRepair(1, RepairApprovalStatus.AKKOORD);

        repairOrderService.addAgreement(new RepairOrderDto(null,
                "Alles repareren",null
        ),1);

        repairOrderLineService.addRepairOrderLaborItem(1,"H0000");

        log.info("Monteur is de de auto aan het repareren");

          /*
            Voeg onderdelen en handelingen toe aan bestel-regels aan klant met id klantid 1
         */
        log.info("Monteur heeft de reparatie voltooid en voegt onderdelen toe aan de reparatieorder");

        repairOrderLineService.addRepairOrderItem(1,"P001",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP001");

        repairOrderLineService.addRepairOrderItem(1, "P002",1);
        repairOrderLineService.addRepairOrderLaborItem(1,"HP002");

        repairOrderLineService.addRepairOrderItem(1, "P003",1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP003");

        repairOrderLineService.addRepairOrderItem(1,"P004", 1);
        repairOrderLineService.addRepairOrderLaborItem(1, "HP004");

        repairOrderService.setStatus(1, RepairStatus.VOLTOOID);
    }
}
