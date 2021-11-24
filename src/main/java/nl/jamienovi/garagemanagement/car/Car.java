package nl.jamienovi.garagemanagement.car;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import nl.jamienovi.garagemanagement.appointment.Appointment;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Class represents core information about the Car entity
 *
 * @version 1.1 10 Sept 2021
 */
@Entity(name = "Car")
@Table(name = "auto")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @SequenceGenerator(name = "car_sequence", sequenceName = "car_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "car_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "merk")
    @NotBlank(message = "Merk van auto niet ingevoerd.")
    private String brand;

    @NotBlank(message = "Model auto niet ingevoerd.")
    @Column(name = "model")
    private String model;

    @Column(name = "kenteken",unique = true)
    @NotBlank(message = "Kenteken invoeren verplicht.")
    private String registrationPlate;

    // Serialize Object by its id instead of full POJO
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    // Serialize Object by its id instead of full POJO
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(orphanRemoval = true,mappedBy = "car",cascade = CascadeType.ALL)
    private List<InspectionReport> inspectionReports;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    private Appointment appointment;

    public Car(String brand, String model, String registrationPlate) {
        this.brand = brand;
        this.model = model;
        this.registrationPlate = registrationPlate;
    }
}
