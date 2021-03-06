package nl.jamienovi.garagemanagement.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.car.Car;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Class represents core information about Appointment entity
 * @version 1 30 Oct 2021
 * @author Jamie Spekman
 */
@Entity(name = "Appointment")
@Table(name = "afspraken")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    @SequenceGenerator(name = "appointment_sequence",sequenceName = "appointment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "appointment_sequence")
    @Column(name = "id",updatable = false)
    private Integer id;

    @Column(name = "datum")
    private LocalDate date;

    @Column(name = "tijd")
    private LocalTime time;

    @Column(name = "soort")
    @Enumerated(EnumType.STRING)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private AppointmentType type;

    // Serialize Object by its id instead of full POJO
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car car;

    public Appointment(LocalDate date, LocalTime time, AppointmentType type) {
        this.date = date;
        this.time = time;
        this.type = type;
    }
}
