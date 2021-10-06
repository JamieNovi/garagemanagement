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

    public CustomerDataLoader(CustomerRepository customerRepository,
                              InspectionService inspectionService,
                              CarPartRepository carPartRepository, LaborRepository laborRepository) {
        this.customerRepository = customerRepository;
        this.inspectionService = inspectionService;
        this.carPartRepository = carPartRepository;
        this.laborRepository = laborRepository;
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
        customerRepository.saveAll(customers);

        // Add inspectionreport to customer Tom Cruise
        InspectionReport report = new InspectionReport();
        report.setCar(carsTomCruise.get(0));
        inspectionService.addInspectionReportToCar(1, report);

        // Add car parts to database
        CarPart carPart = new CarPart("Remschijven",49.99,15);
        CarPart exhaust = new CarPart("Uitlaat",87.50,4);
        carPartRepository.saveAll(List.of(carPart,exhaust));

        // Add labor items to database
        Labor laborDiscBrakes = new Labor("Arbeid Remschijven", 32.50);
        Labor laborExhaust = new Labor("Arbeid Uitlaat monteren", 17.75);
        laborRepository.saveAll(List.of(laborDiscBrakes,laborExhaust));


        log.info("Seeding data succeeded");
    }
}
