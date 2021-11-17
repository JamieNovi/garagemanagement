package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Customer")
@Table(name = "Klanten")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_sequence",sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "customer_sequence")
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

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class,property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @Column(name= "cars")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "customer")
    //@JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Set<Car> cars = new HashSet<>();

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class,property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<RepairOrder> repairOrders;

    public Customer(String firstName, String lastName,String phoneNumber ,String email, String address,
                    String postalCode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    // == Cunstructor voor unit en integratie tests ==

    public Customer(Integer id, String firstName, String lastName, String email, String address,
                    String postalCode, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;

    }

    public void addCar(final Car car) {
        car.setCustomer(this);
        this.cars.add(car);
    }

}
