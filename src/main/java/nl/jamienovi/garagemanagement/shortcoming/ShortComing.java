package nl.jamienovi.garagemanagement.shortcoming;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.jamienovi.garagemanagement.inspection.InspectionReport;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity(name="ShortComing")
@Table(name = "tekortkomingen")
@NoArgsConstructor
@Getter
@Setter
public class ShortComing {
    @Id
    @SequenceGenerator(name = "shortcoming_sequence",sequenceName = "shortcoming_sequence")
    @GeneratedValue(strategy = SEQUENCE, generator = "shortcoming_sequence")
    @Column(name="tekortkoming_id", updatable = false)
    private Integer id;

    @Column(name = "beschrijving")
    private String description;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name= "keuringsrapport_id")
    private InspectionReport inspectionReport;

    public ShortComing(String description) {
        this.description = description;
    }
}
