package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.appointment.AppointmentService;
import nl.jamienovi.garagemanagement.appointment.AppointmentType;
import nl.jamienovi.garagemanagement.authentication.ApplicationUser;
import nl.jamienovi.garagemanagement.authentication.ApplicationUserRepository;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.security.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Order(2)
@Slf4j
@Transactional
public class CustomerDataLoader implements CommandLineRunner {

    private final CustomerService customerService;
    private final AppointmentService appointmentService;
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder encoder;

    public CustomerDataLoader(CustomerService customerService,
                              AppointmentService appointmentService,
                              ApplicationUserRepository applicationUserRepository,
                              PasswordEncoder encoder) {
        this.customerService = customerService;
        this.appointmentService = appointmentService;
        this.applicationUserRepository = applicationUserRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {

        /*
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
               "06-43244244",
               "tomcruise@hollywood.com",
               "Hollywood boulevard 131",
               "90024", "Los Angeles"
                );
        customer1.addCar(car1);
        customerService.saveCustomer(customer1);

        appointmentService.save(1,new Appointment(
                LocalDate.of(2021,10,27),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer2 = new Customer(
               "Leonardo",
                "Dicaprio",
                "06-23423424",
                "leo@hollywood.com",
                "Beverly Hills",
                "90211",
                "Los Angeles"
                );
        customer2.addCar(car2);
        customerService.saveCustomer(customer2);

        appointmentService.save(2,new Appointment(
                LocalDate.of(2021,11,11),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer3 = new Customer(
                "Julie",
                "Cash",
                "089-2342424",
                "juliecash@bookings.com",
                "Avenue 5",
                "24245",
                "Texas"
       );
        customer3.addCar(car3);
        customerService.saveCustomer(customer3);

//        appointmentService.save(3,new Appointment(
//                LocalDate.of(2021,11,11),
//                LocalTime.of(12,30,00),
//                AppointmentType.KEURING
//        ));
//
//        appointmentService.save(5,new Appointment(
//                LocalDate.of(2021,10,31),
//                LocalTime.of(18,30,00),
//                AppointmentType.KEURING
//        ));


        // == Gebruiker toevoegenen aan database ==
        ApplicationUser frontOfficeMedewerker = new ApplicationUser(
                "front-office",
                encoder.encode("1234"),
                UserRole.FRONTOFFICE
        );
        applicationUserRepository.save(frontOfficeMedewerker);

        ApplicationUser administratieMedewerker = new ApplicationUser(
                "administratie",
                encoder.encode("1234"),
                UserRole.ADMIN
        );

        applicationUserRepository.save(administratieMedewerker);

        ApplicationUser monteur = new ApplicationUser(
                "monteur",
                encoder.encode("1234"),
                UserRole.MECHANIC
        );

        applicationUserRepository.save(monteur);

        ApplicationUser backOfficeMedewerker = new ApplicationUser(
                "back-office",
                encoder.encode("1234"),
                UserRole.BACKOFFICE
        );

        applicationUserRepository.save(backOfficeMedewerker);



    }
}
