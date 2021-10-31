package nl.jamienovi.garagemanagement.customer;

import lombok.*;
import nl.jamienovi.garagemanagement.car.Car;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {
      private int id;
      private String firstName;
      private String lastName;
      private String email;
      private String address;
      private String postalCode;
      private String city;
      private List<Car> cars;

      public CustomerUpdateDto(int id, String firstName, String lastName, String email, String address,
                               String postalCode, String city) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.address = address;
            this.postalCode = postalCode;
            this.city = city;
      }
}
