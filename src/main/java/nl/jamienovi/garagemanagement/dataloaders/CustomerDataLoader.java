package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
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

    public CustomerDataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Customer> customers = new ArrayList<>();
       List<Car> carsTomCruise = Arrays.asList(new Car("Ferrari","California","FJ-XZ-88"));
        List<Car> carsLeonardo = Arrays.asList(new Car("Mercedes-Benz","GLC","DS-SJ-66"));
        List<Car> carsJulieCash = Arrays.asList(new Car("BMW","I7","HM-XXX-69"));

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
                "90210",
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

        log.info("Seeding customers to database succeeded");
    }
}
