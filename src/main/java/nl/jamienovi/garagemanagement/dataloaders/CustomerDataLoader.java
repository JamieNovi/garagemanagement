package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.appointment.AppointmentService;
import nl.jamienovi.garagemanagement.appointment.AppointmentType;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import nl.jamienovi.garagemanagement.invoice.InvoiceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Order(2)
@Slf4j
@Transactional
public class CustomerDataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final InvoiceService invoiceService;
    private final AppointmentService appointmentService;

    public CustomerDataLoader(CustomerRepository customerRepository, InvoiceService invoiceService, AppointmentService appointmentService) {
        this.customerRepository = customerRepository;
        this.invoiceService = invoiceService;
        this.appointmentService = appointmentService;
    }

    @Override
    public void run(String... args) throws Exception {
        /**
         * Hier worden alle klanten met auto's aangemaakt die als testdata in de database
         * worden opgeslagen. Alle endpoints zullen JSON responses teruggeven van deze testdata.
         */

        Car car1 = new Car("Ferrari","California","FJ-XZ-88");
        Car car2 = new Car("Mercedes-Benz","GLC","DS-SJ-66");
        Car car3 =  new Car("BMW","I7","HM-XXX-69");
        Car car4 = new Car("Audi","R8","87-23-WE");
        Car car5 = new Car("Ford","Mustang","HM-00-JJ");

        Customer customer1 = new Customer(
               "Tom",
               "Cruise",
               "tomcruise@hollywood.com",
               "Hollywood boulevard 131",
               "90024", "Los Angeles"
                );
        customer1.addCar(car1);
        customer1.addCar(car2);
        customer1 = customerRepository.save(customer1);

        appointmentService.save(1,new Appointment(
                LocalDate.of(2021,10,27),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer2 = new Customer(
               "Leonardo",
                "Dicaprio",
                "leo@hollywood.com",
                "Beverly Hills",
                "90211",
                "Los Angeles"
                );
        customer2.addCar(car3);
        customer2 = customerRepository.save(customer2);

        appointmentService.save(3,new Appointment(
                LocalDate.of(2021,11,11),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer3 = new Customer(
                "Julie",
                "Cash",
                "juliecash@bookings.com",
                "Avenue 5",
                "24245",
                "Texas"
       );
        customer3.addCar(car4);
        customer3.addCar(car5);
        customer3 = customerRepository.save(customer3);

        appointmentService.save(4,new Appointment(
                LocalDate.of(2021,11,11),
                LocalTime.of(12,30,00),
                AppointmentType.KEURING
        ));

        appointmentService.save(5,new Appointment(
                LocalDate.of(2021,10,31),
                LocalTime.of(18,30,00),
                AppointmentType.KEURING
        ));



    }
}
