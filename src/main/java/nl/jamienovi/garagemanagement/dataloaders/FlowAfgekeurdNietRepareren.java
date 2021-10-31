package nl.jamienovi.garagemanagement.dataloaders;

import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.inspection.InspectionStatus;
import nl.jamienovi.garagemanagement.invoice.InvoiceService;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderService;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import nl.jamienovi.garagemanagement.shortcoming.ShortComingRepository;
import org.springframework.boot.CommandLineRunner;

@Slf4j
//@Component
//@Order(3)
//@Transactional
public class FlowAfgekeurdNietRepareren implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;
    private final InspectionService inspectionService;
    private final ShortComingRepository shortComingRepository;
    private final RepairOrderLineService repairOrderLineService;
    private final RepairOrderService repairOrderService;

    public FlowAfgekeurdNietRepareren(CustomerRepository customerRepository,
                                      InvoiceService invoiceService,
                                      InspectionService inspectionService, ShortComingRepository shortComingRepository, RepairOrderLineService repairOrderLineService, RepairOrderService repairOrderService) {
        this.customerRepository = customerRepository;
        this.invoiceService = invoiceService;
        this.inspectionService = inspectionService;
        this.shortComingRepository = shortComingRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.repairOrderService = repairOrderService;
    }

    @Override
    public void run(String... args) throws Exception {

        Car car2 = new Car("Mercedes-Benz","GLC","DS-SJ-66");

        Customer customer2 = new Customer(
                "Leonardo",
                "Dicaprio",
                "leo@hollywood.com",
                "Beverly Hills",
                "90211",
                "Los Angeles"
        );
        customer2.addCar(car2);
        customer2 = customerRepository.save(customer2);


        inspectionService.addInspectionReportToCar(3);

        repairOrderLineService.addRepairOrderLaborItem(2,"H0000");

        inspectionService.setInspectionReportStatus(2, InspectionStatus.GOEDGEKEURD);

        //repairOrderService.setStatus(2,RepairStatus.VOLTOOID);

    }
}
