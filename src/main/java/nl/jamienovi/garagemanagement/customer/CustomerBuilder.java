package nl.jamienovi.garagemanagement.customer;

public class CustomerBuilder {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String postalCode;
    private String city;
//
//    public CustomerBuilder(Integer id, String firstName, String lastName, String phoneNumber,
//                           String email, String address, String postalCode, String city) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phoneNumber = phoneNumber;
//        this.email = email;
//        this.address = address;
//        this.postalCode = postalCode;
//        this.city = city;
//    }

    public CustomerBuilder setId(Integer id){
        this.id = id;
        return this;
    }

    public CustomerBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CustomerBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder setAddress(String address) {
        this.address = address;
        return this;
    }
    public CustomerBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }
    public CustomerBuilder setCity(String city) {
        this.city = city;
        return this;
    }
    public Customer build() {
        return new Customer(
                id,
                firstName,
                lastName,
                phoneNumber,
                email,
                address,
                postalCode,
                city
        );

    }
}
