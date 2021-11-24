package nl.jamienovi.garagemanagement.customer;

import nl.jamienovi.garagemanagement.utils.Builder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository undertest;

    @AfterEach
    void tearDown() {
        undertest.deleteAll();
    }

    @Test
    void itShouldCheckIfEmailAlreadyExists() {
        //Arrange
        String email = "famkejansen@hotmail.com";
        Customer customer = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Famke"))
                .with(s -> s.setLastName("Janssen"))
                .with(s -> s.setPhoneNumber("0632324"))
                .with(s -> s.setEmail(email))
                .with(s -> s.setAddress("Keizersgracht 10"))
                .with(s -> s.setPostalCode("1002 AB"))
                .with(s -> s.setCity("Amsterdam"))
                .get();
        undertest.save(customer);

        //Act
        Boolean expected = undertest.emailAlreadyExists(email);

        //Assert
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfEmailDoesNotExists() {
        //Arrange
        String email = "famkejansen212@hotmail.com";

        //Act
        Boolean exists = undertest.emailAlreadyExists(email);

        //Assert
        assertThat(exists).isFalse();
    }
}