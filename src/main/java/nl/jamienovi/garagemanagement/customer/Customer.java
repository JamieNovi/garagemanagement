package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Customer")
@Table(name = "Klanten")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
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

//    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property="id")
//    @JsonIdentityReference(alwaysAsId=true)
    @Column(name= "cars")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ToString.Exclude
    private List<Car> cars = new ArrayList<>();

//    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,property="id")
//    @JsonIdentityReference(alwaysAsId=true)
    @JsonIgnore
    @OneToOne(mappedBy = "customer")
    private RepairOrder repairOrder;

    @OneToOne(mappedBy = "customer")
    private Appointment appointment;

    public Customer(String firstName, String lastName, String email, String address,
                    String postalCode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

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

    /**
     * Voorziening om bidirectional relatie aan beide kanten te updaten
     * @param car
     */
    public void addCar(final Car car) {
        car.setCustomer(this);
        this.cars.add(car);
    }


}
