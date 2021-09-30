package nl.jamienovi.garagemanagement.car;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.customer.Customer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Cars")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Car {

    @Id
    @SequenceGenerator(name = "car_sequence", sequenceName = "car_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "car_sequence")
    @Column(updatable = false)
    private Integer id;

    @Column(name = "brand")
    @NotBlank(message = "Merk van auto niet ingevoerd.")
    private String brand;

    @NotBlank(message = "Model auto niet ingevoerd.")
    @Column(name = "model")
    private String model;

    @Column(name = "registration_plate",unique = true)
    @NotBlank(message = "Kenteken invoeren verplicht.")
    private String registrationPlate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Customer customer;

    public Car(String brand, String model, String registrationPlate) {
        this.brand = brand;
        this.model = model;
        this.registrationPlate = registrationPlate;
    }


}
