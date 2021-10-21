package nl.jamienovi.garagemanagement.inspection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "afspraak")
    @CreationTimestamp
    private LocalDateTime afspraak;

    @OneToMany(mappedBy = "inspectionReport",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShortComing> shortcomings;

    //@Setter(AccessLevel.NONE)
    @JsonIgnoreProperties({"customer","brand","model","registrationPlate","inspectionReports"})
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "auto_id")
    private Car car;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InspectionStatus status = InspectionStatus.IN_BEHANDELING;

    @JsonIgnore
    @OneToOne(cascade = { CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY, mappedBy = "inspectionReport")
    private RepairOrder repairOrder;

    /**
     * Voorziening om bidirectional relatie aan beide kanten te updaten
     * @param shortComing
     */
    public void addShortComing(final ShortComing shortComing){
        shortComing.setInspectionReport(this);
        this.shortcomings.add(shortComing);
    }

//    public void setCar(Car car) {
//        if(car != null){
//            car.getInspectionReports().add(this);
//        }else if (car != null) {
//            this.car.getInspectionReports().remove(this);
//        }
//        this.car = car;
//    }

}
