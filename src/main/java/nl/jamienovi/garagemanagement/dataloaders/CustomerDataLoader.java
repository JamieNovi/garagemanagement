package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import nl.jamienovi.garagemanagement.inspection.InspectionReportRepository;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.labor.Labor;
import nl.jamienovi.garagemanagement.labor.LaborRepository;
import nl.jamienovi.garagemanagement.part.Part;
import nl.jamienovi.garagemanagement.part.PartRepository;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import nl.jamienovi.garagemanagement.shortcoming.ShortComingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
@Slf4j
public class CustomerDataLoader implements CommandLineRunner {

    private CustomerRepository customerRepository;
    private final InspectionReportRepository inspectionRepository;
    private final PartRepository carPartRepository;
    private final LaborRepository laborRepository;
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderLineService repairOrderLineService;
    private final RepairOrderRepository repairOrderRepository;
    private final ShortComingService shortComingService;
    private final InspectionService inspectionService;

    @Autowired
    public CustomerDataLoader(CustomerRepository customerRepository,
                              InspectionReportRepository inspectionReportRepository,
                              PartRepository carPartRepository,
                              LaborRepository laborRepository,
                              RepairOrderLineRepository repairOrderLineRepository,
                              RepairOrderLineService repairOrderLineService,
                              RepairOrderRepository repairOrderRepository,
                              ShortComingService shortComingService,
                              InspectionService inspectionService) {
        this.customerRepository = customerRepository;
        this.inspectionRepository = inspectionReportRepository;
        this.carPartRepository = carPartRepository;
        this.laborRepository = laborRepository;
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.repairOrderRepository = repairOrderRepository;
        this.shortComingService = shortComingService;
        this.inspectionService = inspectionService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create cars
        List<Customer> customers = new ArrayList<>();
        List<Car> carsTomCruise = Arrays.asList(new Car("Ferrari","California","FJ-XZ-88"));
        List<Car> carsLeonardo = Arrays.asList(new Car("Mercedes-Benz","GLC","DS-SJ-66"));
        List<Car> carsJulieCash = Arrays.asList(new Car("BMW","I7","HM-XXX-69"));

        // Create customers with cars
        customers.add(new Customer(
               "Tom",
               "Cruise",
               "tomcruise@hollywood.com",
               "Hollywood boulevard 131",
               "90024", "Los Angeles",
                carsTomCruise
                ));

        customers.add(new Customer(
               "Leonardo",
                "Dicaprio",
                "leo@hollywood.com",
                "Beverly Hills",
                "90211",
                "Los Angeles",
                carsLeonardo));

        customers.add(new Customer(
                "Julie",
                "Cash",
                "juliecash@bookings.com",
                "Avenue 5",
                "24245",
                "Texas",
                carsJulieCash));

        log.info("Seeding customerdata with cars");
        customerRepository.saveAll(customers);

        // Add car parts to database
        Part diskBrakes = new Part("P001","Remschijven",49.99,15);
        Part exhaust = new Part("P002","Uitlaat",87.50,4);
        Part oilFilter = new Part("P003","Oliefilter",9.95,12);
        Part sparkPlug = new Part("P004","Bougie",14.34, 20);
        Part headLight = new Part("P005","Koplamp",50.22,2);

        // Add labor items to database
        Labor inspection = new Labor("H0000","Kosten keuring", 50.00);
        Labor laborDiscBrakes = new Labor("HP001","Montagekosten Remschijven", 32.50);
        Labor laborExhaust = new Labor("HP002","Montagekosten uitlaat", 17.75);
        Labor labourOilFilter = new Labor("HP003","Montagekosten oliefilter vervangen",12.99);
        Labor labourSparkPlug = new Labor("HP004","Montagekosten vervangen", 8.22);
        Labor labourHeadLight = new Labor("HP005","Montagekosten koplamp vervangen",34.01);

        log.info("Seeding laboritems to database");
        laborRepository.saveAll(List.of(inspection,
                laborDiscBrakes,laborExhaust,
                labourOilFilter,labourSparkPlug,
                labourHeadLight
        ));
        log.info("Seeding carparts to database");
        carPartRepository.saveAll(List.of(
                diskBrakes,exhaust,oilFilter,sparkPlug,headLight));

        log.info("Seeding data succeeded");
    }
}
