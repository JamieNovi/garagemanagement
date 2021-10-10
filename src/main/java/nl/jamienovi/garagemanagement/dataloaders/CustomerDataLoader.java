package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.carpart.CarPart;
import nl.jamienovi.garagemanagement.carpart.CarPartRepository;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import nl.jamienovi.garagemanagement.inspection.InspectionService;
import nl.jamienovi.garagemanagement.laboritem.Labor;
import nl.jamienovi.garagemanagement.laboritem.LaborRepository;
import nl.jamienovi.garagemanagement.repairorder.RepairOrderRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineRepository;
import nl.jamienovi.garagemanagement.repairorderline.RepairOrderLineService;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
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
    private final InspectionService inspectionService;
    private final CarPartRepository carPartRepository;
    private final LaborRepository laborRepository;
    private final RepairOrderLineRepository repairOrderLineRepository;
    private final RepairOrderLineService repairOrderLineService;
    private final RepairOrderRepository repairOrderRepository;
    private final ShortComingService shortComingService;

    @Autowired
    public CustomerDataLoader(CustomerRepository customerRepository,
                              InspectionService inspectionService,
                              CarPartRepository carPartRepository,
                              LaborRepository laborRepository,
                              RepairOrderLineRepository repairOrderLineRepository,
                              RepairOrderLineService repairOrderLineService,
                              RepairOrderRepository repairOrderRepository,
                              ShortComingService shortComingService) {
        this.customerRepository = customerRepository;
        this.inspectionService = inspectionService;
        this.carPartRepository = carPartRepository;
        this.laborRepository = laborRepository;
        this.repairOrderLineRepository = repairOrderLineRepository;
        this.repairOrderLineService = repairOrderLineService;
        this.repairOrderRepository = repairOrderRepository;
        this.shortComingService = shortComingService;
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
        CarPart diskBrakes = new CarPart("Remschijven",49.99,15);
        CarPart exhaust = new CarPart("Uitlaat",87.50,4);
        CarPart oilFilter = new CarPart("Oliefilter",9.95,12);
        CarPart sparkPlug = new CarPart("Bougie",14.34, 20);
        CarPart headLight = new CarPart("Koplamp",50.22,2);

        log.info("Seeding carparts to database");
        carPartRepository.saveAll(List.of(
                diskBrakes,exhaust,oilFilter,sparkPlug,headLight));

        // Add labor items to database
        Labor inspection = new Labor("Kosten keuring", 50.00);
        Labor laborDiscBrakes = new Labor("Arbeid Remschijven", 32.50);
        Labor laborExhaust = new Labor("Arbeid Uitlaat monteren", 17.75);
        Labor labourOilFilter = new Labor("Arbeid oliefilter vervangen",12.99);
        Labor labourSparkPlug = new Labor("Arbeid bougie vervangen", 8.22);
        Labor labourHeadLight = new Labor("Arbeid koplamp vervangen",34.01);

        log.info("Seeding laboritems to database");
        laborRepository.saveAll(List.of(inspection,
                laborDiscBrakes,laborExhaust,
                labourOilFilter,labourSparkPlug,
                labourHeadLight
        ));

        /*
         Keuringsrapport en reparatieorder toevoegen aan auto(id=1) van klant Tom Cruise en Julie Cash(id=2).
         Reparatieorder(id=1 voor Tom en id=2 voor Julie)
         wordt gelijk aan gemaakt om keuringstarief toe te kunnen toevoegen.
        */
        InspectionReport report = new InspectionReport();
        report.setCar(carsTomCruise.get(0));
        inspectionService.addInspectionReportToCar(carsTomCruise.get(0).getId(), report);

        InspectionReport report2 = new InspectionReport();
        report2.setCar(carsJulieCash.get(0));
        inspectionService.addInspectionReportToCar(carsJulieCash.get(0).getId(),report2);

        /*
            Voeg tekortkoming toe aan keuringsrapporten
         */

        shortComingService.addShortComing(1,new ShortComing("Uitlaat defect."));

        shortComingService.addShortComing(2,new ShortComing("Bougies kapot"));


        log.info("Keuringsrapport toegevoegd aan auto met id 1(Tom Cruise) en Julie Cash");

        /*
            Voeg keuringstarief regel toe aan de reparatie-regels van reparatieorder(id=1)
            van klant(id=1) Tom Cruise
         */
        Labor inspectionRateItem = laborRepository.getById(1);
        repairOrderLineService.addRepairOrderLaborItem(1,inspectionRateItem);
        ;
        repairOrderLineService.addRepairOrderLaborItem(2,inspectionRateItem);

        /*
            Voeg onderdelen en handelingen toe aan bestel-regels
         */
        repairOrderLineService.addRepairOrderItem(1,carPartRepository.getById(1));
        repairOrderLineService.addRepairOrderLaborItem(1,laborRepository.getById(2));

        repairOrderLineService.addRepairOrderItem(1,carPartRepository.getById(2));
        repairOrderLineService.addRepairOrderLaborItem(1,laborRepository.getById(3));

        repairOrderLineService.addRepairOrderItem(1,carPartRepository.getById(3));
        repairOrderLineService.addRepairOrderLaborItem(1, laborRepository.getById(3));

        repairOrderLineService.addRepairOrderItem(1,carPartRepository.getById(4));
        repairOrderLineService.addRepairOrderLaborItem(1, laborRepository.getById(4));

        repairOrderLineService.addRepairOrderItem(2,carPartRepository.getById(4));


        log.info("Seeding data succeeded");
    }
}
