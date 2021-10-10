package nl.jamienovi.garagemanagement.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nl.jamienovi.garagemanagement.repairorder.RepairStatus;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceCustomerDataDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String email;
    private String brand;
    private String model;
    private String registrationPlate;
    private Integer repairOrderId;
    private RepairStatus status;
}
