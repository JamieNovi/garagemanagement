package nl.jamienovi.garagemanagement.car;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Car")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@ToString
@NoArgsConstructor
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


    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = InspectionReport.class,orphanRemoval = true)
    @JoinColumn(name="car_id", referencedColumnName = "id")
    private List<InspectionReport> inspectionReports;

    public Car(String brand, String model, String registrationPlate) {
        this.brand = brand;
        this.model = model;
        this.registrationPlate = registrationPlate;
    }


}
