package nl.jamienovi.garagemanagement.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.customer.Customer;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static javax.persistence.GenerationType.SEQUENCE;

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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name ="klant_id", referencedColumnName = "id")
    private Customer customer;
}
