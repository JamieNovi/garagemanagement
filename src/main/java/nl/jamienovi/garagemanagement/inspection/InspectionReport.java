package nl.jamienovi.garagemanagement.inspection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.car.Car;
import nl.jamienovi.garagemanagement.repairorder.RepairOrder;
import nl.jamienovi.garagemanagement.shortcoming.ShortComing;
import nl.jamienovi.garagemanagement.utils.BooleanJaNeeConverter;
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

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "auto_id")
    private Car car;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InspectionStatus status = InspectionStatus.IN_BEHANDELING;

    @Column(name = "akkoord_reparatie")
    @Enumerated(EnumType.STRING)
    private RepairApprovalStatus repairApprovalStatus;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToOne(cascade = { CascadeType.REMOVE,CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY, mappedBy = "inspectionReport")
    private RepairOrder repairOrder;

    @Column(name = "Gerepareerd")
    @Convert(converter = BooleanJaNeeConverter.class)
    private Boolean isRepaired = false;

}
