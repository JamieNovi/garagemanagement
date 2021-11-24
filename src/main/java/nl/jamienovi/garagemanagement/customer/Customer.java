package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents core information about the customer
 *
 * @version 1.1 10 Sept 2021
 * @author Jamie Spekman
 */
@Entity(name = "Customer")
@Table(name = "klanten")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {

    @Id
    @SequenceGenerator(name = "customer_sequence",sequenceName = "customer_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "voornaam",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Voornaam is verplicht.")
    private String firstName;

    @Column(name = "achternaam",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Achternaam is verplicht.")
    private String lastName;

    @Column(name = "telefoonnr")
    @NotBlank(message = "Telefoonnummer verplicht.")
    private String phoneNumber;

    @Column(name = "email")
    @Email(message = "Geen geldig emailadres ingevoerd.")
    private String email;

    @Column(name = "adres",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Adres is verplicht.")
    private String address;

    @Column(name = "postcode",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Postcode is verplicht.")
    private String postalCode;

    @Column(name = "woonplaats",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Woonplaats is verplicht.")
    private String city;

    // Serialize Object by its id instead of full POJO
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Set<Car> cars = new HashSet<>();

    @JsonIgnoreProperties({"createdAt", "agreementComments", "inspectionReport","repairOrderLines","customer"})
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<RepairOrder> repairOrders;

    public void addCar(final Car car) {
        car.setCustomer(this);
        this.cars.add(car);
    }
}
