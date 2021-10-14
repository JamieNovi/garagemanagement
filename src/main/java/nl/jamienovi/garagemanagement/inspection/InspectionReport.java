package nl.jamienovi.garagemanagement.inspection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "inspectionReport",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortComing> shortcomings;

    @JsonIgnoreProperties({"customer","brand","model","registrationPlate","inspectionReports"})
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @JsonIgnore
    @OneToOne(cascade = { CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
    @JoinColumn(name="reparatie_id",referencedColumnName = "reparatie_id")
    private RepairOrder repairOrder;




}
