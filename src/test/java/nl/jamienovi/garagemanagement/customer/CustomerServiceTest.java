package nl.jamienovi.garagemanagement.customer;

import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private CustomerService underTest;

    // Mock
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DtoMapper mapper;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerRepository,mapper);
    }

    @Test
    void canGetAllCustomers() {
        //Act
        underTest.getAllCustomers();
        //Assert
        verify(customerRepository).findAll();
    }

    @Test
    void shouldGetCustomer() throws Exception {
        //Arrange
        String email = "famkejansen@hotmail.com";
        Customer mockCustomer = new Customer(
                1,
                "Famke",
                "Janssen",
                email,
                "Keizersgracht 10",
                "1002 AB",
                "Amsterdam"
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        underTest.getCustomer(1);
        //Assert
        verify(customerRepository).findById(1);
    }

//    @Test
//    void saveCustomer() {
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
//        underTest.saveCustomer(mockCustomer);
//        //Assert
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
//        verify(customerRepository).save(customerArgumentCaptor.capture());
//
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//        assertThat(capturedCustomer).isEqualTo(mockCustomer);
//    }

    @Test
    void deleteCustomer() {
        //Arrange
        Customer mockCustomer = new Customer(
                1,
                "Famke",
                "Janssen",
                "famkejansen@mail.com",
                "Keizersgracht 10",
                "1002 AB",
                "Amsterdam"
        );
        //Act
        when(customerRepository.existsById(mockCustomer.getId())).thenReturn(true);
        underTest.deleteCustomer(mockCustomer.getId());
        verify(customerRepository).deleteById(mockCustomer.getId());
    }

    @Test
    void deleteByIdThrowsEntityNotFoundException() {
        Integer customerId = 1;
        when(customerRepository.existsById(1)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> underTest.deleteCustomer(customerId));

    }

    @Test
    void updateCustomerNotFoundShouldThrowEntityNotFoundException() {
        Integer customerId = 1;
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> underTest.updateCustomer(customerId,any()));

    }

    @Test
    void getCustomerShouldThrowEntityNotFoundException() {
        Integer customerId = 1;
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> underTest.getCustomer(customerId));
    }
}