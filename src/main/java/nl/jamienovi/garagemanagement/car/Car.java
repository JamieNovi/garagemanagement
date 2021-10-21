package nl.jamienovi.garagemanagement.car;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Car")

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


    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(orphanRemoval = true,mappedBy = "car", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<InspectionReport> inspectionReports;

    public Car(String brand, String model, String registrationPlate) {
        this.brand = brand;
        this.model = model;
        this.registrationPlate = registrationPlate;
    }

    public Car(Integer id, String brand, String model, String registrationPlate) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.registrationPlate = registrationPlate;
    }

/**
     * Voorziening om bidirectional relatie aan beide kanten te updaten
     * @param customer
     */
//    public void setCustomer(Customer customer) {
//        if(customer != null) {
//            customer.getCars().add(this);
//        }else if (this.customer != null) {
//            this.customer.getCars().remove(this);
//        }
//        this.customer = customer;
//    }



}
