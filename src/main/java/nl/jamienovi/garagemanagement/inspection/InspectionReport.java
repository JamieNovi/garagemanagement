package nl.jamienovi.garagemanagement.inspection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name = "InspectionReport")
@Table(name = "keuringsrapport")
@Getter
@Setter
@NoArgsConstructor
public class InspectionReport {

    @Id
    @SequenceGenerator(name="inspection_report_sequence", sequenceName = "inspection_report_sequence",allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "inspection_report_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name= "inspection_report_id", referencedColumnName = "id")
    private List<ShortComing> shortcomings;

   @JsonIgnoreProperties({"customer","brand","model","registrationPlate","inspectionReports"})
    @ManyToOne(targetEntity = Car.class,cascade = CascadeType.MERGE)
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="repairorder_id",referencedColumnName = "id")
    private RepairOrder repairOrder = new RepairOrder("Geen afspraken.");




}
