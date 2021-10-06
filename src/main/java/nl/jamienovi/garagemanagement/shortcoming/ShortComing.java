package nl.jamienovi.garagemanagement.shortcoming;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name="ShortComing")
@Table(name = "tekortkomingen")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShortComing {
    @Id
    @SequenceGenerator(name = "shortcoming_sequence",sequenceName = "shortcoming_sequence")
    @GeneratedValue(strategy = SEQUENCE, generator = "shortcoming_sequence")
    private int id;

    @Column(name = "aangemaakt_op")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "beschrijving")
    private String description;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private InspectionReport inspectionReport;

    public ShortComing(String description) {
        this.description = description;
    }

}
