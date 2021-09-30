package nl.jamienovi.garagemanagement.inspection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nl.jamienovi.garagemanagement.car.Car;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Inspection_reports")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionReport {

    @Id
    @SequenceGenerator(name="inspection_report_sequence", sequenceName = "inspection_report_sequence")
    @GeneratedValue(strategy = SEQUENCE, generator = "inspection_report_sequence")
    private Integer id;

    @Column(name = "date")
    private Date date;

    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    @Column(name= "reparatie_status")
    private RepairStatus status;

}
