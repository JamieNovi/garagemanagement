package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
@Slf4j
public class CustomerDataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    public CustomerDataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        /**
         * Hier worden alle klanten met auto's aangemaakt die als testdata in de database
         * worden opgeslagen. Alle endpoints zullen JSON responses teruggeven van deze testdata.
         */

        List<Customer> customers = new ArrayList<>();

        Car car1 = new Car("Ferrari","California","FJ-XZ-88");
        Car car2 = new Car("Mercedes-Benz","GLC","DS-SJ-66");
        Car car3 =  new Car("BMW","I7","HM-XXX-69");

        Customer customer1 = new Customer(
               "Tom",
               "Cruise",
               "tomcruise@hollywood.com",
               "Hollywood boulevard 131",
               "90024", "Los Angeles"
                );
        customer1.addCar(car1);
        customers.add(customer1);

        Customer customer2 = new Customer(
               "Leonardo",
                "Dicaprio",
                "leo@hollywood.com",
                "Beverly Hills",
                "90211",
                "Los Angeles"
                );
        customer2.addCar(car2);
        customers.add(customer2);

       Customer customer3 = new Customer(
                "Julie",
                "Cash",
                "juliecash@bookings.com",
                "Avenue 5",
                "24245",
                "Texas"
       );
       customer3.addCar(car3);
       customers.add(customer3);

        customerRepository.saveAll(customers);

    }
}
