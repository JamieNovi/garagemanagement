package nl.jamienovi.garagemanagement.dataloaders;


import lombok.extern.slf4j.Slf4j;
import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.appointment.AppointmentType;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerServiceImpl;
import nl.jamienovi.garagemanagement.interfaces.AppointmentService;
import nl.jamienovi.garagemanagement.utils.Builder;
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
    private final CustomerServiceImpl customerServiceImpl;
    private final AppointmentService appointmentService;

    public CustomerDataLoader(CustomerServiceImpl customerServiceImpl,
                              AppointmentService appointmentService) {
        this.customerServiceImpl = customerServiceImpl;
        this.appointmentService = appointmentService;
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
        Car car6 = new Car("Peugeto","RCZ","DS-SJ-JS");

        Customer customer1 = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Tom"))
                .with(s -> s.setLastName("Cruise"))
                .with(s -> s.setPhoneNumber("06-43244244"))
                .with(s -> s.setEmail("tomcruise@hollywood.com"))
                .with(s -> s.setAddress("Hollywood boulevard 131"))
                .with(s -> s.setPostalCode("90024"))
                .with(s -> s.setCity("Los Angeles"))
                .get();
        customer1.addCar(car1);
        customerServiceImpl.add(customer1);

        appointmentService.addAppointmentToCar(1,new Appointment(
                LocalDate.of(2021,10,27),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer2 = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Leonardo"))
                .with(s -> s.setLastName("Dicaprio"))
                .with(s -> s.setPhoneNumber("06-23423424"))
                .with(s -> s.setEmail("leo@hollywood.com"))
                .with(s -> s.setAddress("Beverly Hills"))
                .with(s -> s.setPostalCode("90211"))
                .with(s -> s.setCity("Los Angeles"))
                .get();

        customer2.addCar(car2);
        customerServiceImpl.add(customer2);

        appointmentService.addAppointmentToCar(2,new Appointment(
                LocalDate.of(2021,11,11),
                LocalTime.of(10,15,00),
                AppointmentType.KEURING
        ));

        Customer customer3 = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Julie"))
                .with(s -> s.setLastName("Cash"))
                .with(s -> s.setPhoneNumber("089-2342424"))
                .with(s -> s.setEmail("juliecash@bookings.com"))
                .with(s -> s.setAddress("Avenue 6"))
                .with(s -> s.setPostalCode("242345"))
                .with(s -> s.setCity("Texas"))
                .get();

        customer3.addCar(car3);
        customerServiceImpl.add(customer3);

        appointmentService.addAppointmentToCar(3,new Appointment(
                LocalDate.of(2021,11,11),
                LocalTime.of(12,30,00),
                AppointmentType.KEURING
        ));

        Customer customer4 = Builder.build(Customer.class)
                        .with(s -> s.setFirstName("Ben"))
                        .with(s -> s.setLastName("Greenfield"))
                        .with(s -> s.setPhoneNumber("0632324"))
                        .with(s -> s.setEmail("bengreenfieldfitness@fitness.com"))
                        .with(s -> s.setAddress("River Road 1"))
                        .with(s -> s.setPostalCode("34399"))
                        .with(s -> s.setCity("Spokane"))
                        .get();

        Customer customer5 = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Johnny"))
                .with(s -> s.setLastName("Depp"))
                .with(s -> s.setPhoneNumber("064352534"))
                .with(s -> s.setEmail("dep@mail.com"))
                .with(s -> s.setAddress("Queens boulevard 1"))
                .with(s -> s.setPostalCode("2342"))
                .with(s -> s.setCity("Washington"))
                .get();

        Customer customer6 = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Brad"))
                .with(s -> s.setLastName("Pitt"))
                .with(s -> s.setPhoneNumber("0632332444"))
                .with(s -> s.setEmail("bradpitt@hollywood.com"))
                .with(s -> s.setAddress("Kings boulevard 22"))
                .with(s -> s.setPostalCode("234234"))
                .with(s -> s.setCity("San Diego"))
                .get();

        customerServiceImpl.add(customer4);
        customerServiceImpl.add(customer5);
        customerServiceImpl.add(customer6);

    }
}
