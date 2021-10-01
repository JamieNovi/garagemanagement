package nl.jamienovi.garagemanagement.inspection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name = "inspection_reports")
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
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "short_comings")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name= "inspection_report_id", referencedColumnName = "id")
    private List<ShortComing> shortcomings;

    @ManyToOne(targetEntity = Car.class,cascade = CascadeType.MERGE)
    private Car car;

//    @OneToOne(fetch = FetchType.LAZY, targetEntity = RepairOrder.class)
//    private RepairOrder repairOrder;




}
