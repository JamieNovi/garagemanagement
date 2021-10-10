package nl.jamienovi.garagemanagement.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    public int id;
    public LocalDate created_at;
    public String merk;
    public String model;
    public String kenteken;
}
