package nl.jamienovi.garagemanagement.customer;

import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.Builder;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * De implementatie van CustomerRepository hoeft niet gebruikt te worden,
 * omdat deze al getest is en werkt (out of the box van JPA), hiervoor wordt
 * een mock gebruikt
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    // System under Test (SuT)
    private CustomerServiceImpl underTest;

    // Mock
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DtoMapper mapper;

    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(customerRepository,mapper);
    }

    @Test
    void canGetAllCustomers() {
        //Act
        underTest.findAll();
        //Assert
        verify(customerRepository).findAll();
    }

    @Test
    void shouldGetCustomer() throws Exception {
        //Arrange
        String email = "famkejansen@hotmail.com";
        Customer mockCustomer = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Famke"))
                .with(s -> s.setLastName("Janssen"))
                .with(s -> s.setPhoneNumber("0632324"))
                .with(s -> s.setEmail(email))
                .with(s -> s.setAddress("Keizersgracht 10"))
                .with(s -> s.setPostalCode("1002 AB"))
                .with(s -> s.setCity("Amsterdam"))
                .get();

        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        underTest.findOne(1);
        //Assert
        verify(customerRepository).findById(1);
    }

//    @Test
//    void add() {
//        //Arrange
//        Customer mockCustomer = new Customer(
//                "Famke",
//                "Janssen",
//                "famkejansen@mail.com",
//                "Keizersgracht 10",
//                "1002 AB",
//                "Amsterdam"
//        );
//        //Act
//        underTest.add(mockCustomer);
//        //Assert
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        verify(customerRepository).save(customerArgumentCaptor.capture());
//
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//        assertThat(capturedCustomer).isEqualTo(mockCustomer);
//    }

    @Test
    @Disabled
    void deleteCustomer() {
        //Arrange
        Customer mockCustomer = Builder.build(Customer.class)
                .with(s -> s.setId(1))
                .with(s -> s.setFirstName("Famke"))
                .with(s -> s.setLastName("Janssen"))
                .with(s -> s.setPhoneNumber("0632324"))
                .with(s -> s.setEmail("famkeJansen@mail.com"))
                .with(s -> s.setAddress("Keizersgracht 10"))
                .with(s -> s.setPostalCode("1002 AB"))
                .with(s -> s.setCity("Amsterdam"))
                .get();

        //Act
        when(customerRepository.existsById(mockCustomer.getId())).thenReturn(true);
        underTest.delete(mockCustomer.getId());
        verify(customerRepository).deleteById(mockCustomer.getId());
    }

    @Test
    @Disabled
    void deleteByIdThrowsEntityNotFoundException() throws CustomerEntityNotFoundException {
        Integer customerId = 1;
        when(customerRepository.existsById(1)).thenReturn(false);
        Assertions.assertThrows(CustomerEntityNotFoundException.class, () -> underTest.delete(customerId));
    }

    @Test
    @Disabled
    void updateCustomerNotFoundShouldThrowEntityNotFoundException() {
        Integer customerId = 1;
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerEntityNotFoundException.class, () -> underTest.update(customerId,any()));
    }

    @Test
    void getCustomerShouldThrowEntityNotFoundException() {
        Integer customerId = 1;
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerEntityNotFoundException.class, () -> underTest.findOne(customerId));
    }
}