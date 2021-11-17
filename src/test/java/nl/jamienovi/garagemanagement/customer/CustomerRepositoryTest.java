package nl.jamienovi.garagemanagement.customer;

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
        Customer customer = new Customer(
                "Famke",
                "Janssen",
                "234202342",
                email,
                "Keizersgracht 10",
                "1002 AB",
                "Amsterdam"
        );
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