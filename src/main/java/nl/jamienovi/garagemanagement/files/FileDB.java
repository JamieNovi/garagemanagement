package nl.jamienovi.garagemanagement.files;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Class represents information about uploaded files
 *
 * @version 1 10 Oct 2021
 * @author Jamie Spekman
 */
@Entity(name = "FileDB")
@Table(name = "Autodocumenten")
@Getter
@Setter
@NoArgsConstructor
public class FileDB {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    @Lob
    private byte[] data;

    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
