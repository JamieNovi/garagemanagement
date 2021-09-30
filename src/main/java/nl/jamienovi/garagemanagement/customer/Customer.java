package nl.jamienovi.garagemanagement.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.car.Car;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Customers")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_sequence",sequenceName = "customer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "customer_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "first_name",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Voornaam is verplicht.")
    private String firstName;

    @Column(name = "last_name",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Achternaam is verplicht.")
    private String lastName;

    @Column(name = "email")
    @Email(message = "Geen geldig emailadres ingevoerd.")
    private String email;

    @Column(name = "address",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Adres is verplicht.")
    private String address;

    @Column(name = "postal_code",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Postcode is verplicht.")
    private String postalCode;

    @Column(name = "city",nullable = false,columnDefinition = "TEXT")
    @NotBlank(message = "Woonplaats is verplicht.")
    private String city;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ToString.Exclude
    private List<Car> cars;

    public Customer(String firstName, String lastName, String email, String address,
                    String postalCode, String city, List<Car> cars) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.cars = cars;

    }

}
