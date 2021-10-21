package nl.jamienovi.garagemanagement.car;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerService;
import nl.jamienovi.garagemanagement.errorhandling.EntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.DtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private CarService cut;

    @Test
    void shouldGetAllCars() {
        cut.getAllCars();
        verify(carRepository).findAll();
    }

    @Test
    void shouldGetCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));
        cut.getCar(1);
        verify(carRepository).findById(1);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,() -> cut.getCar(1));
    }

    @Test
    void shouldAddCarToCustomer() {
        Car car = createCar();
        Customer customer = createCustomer();

        when(customerService.getCustomer(1)).thenReturn(customer);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Integer result = cut.addCarToCustomer(1,car);

        assertEquals(customer.getId(),result);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatedCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,() -> cut.updateCarCustomer(1,any()));
    }

    @Test
    void shouldUpdateCarCustomer() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));

        cut.updateCarCustomer(1,any());

        verify(carRepository).save(any());
    }

    @Test
    void shouldDeleteCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));
        cut.deleteCar(1);
        verify(carRepository).deleteById(1);
    }
    @Test
    void shouldThrowEntityNotFoundExceptionWhenDeletedCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,() -> cut.deleteCar(1));
    }

    private CarDto createCarDto() {
        CarDto dto = new CarDto(1,
                "Mini",
                "Cooper",
                "EE-22-22",
                createCustomer()
        );
        return dto;
    }

    private Car createCar() {
        Car car = new Car();
        car.setId(1);
        car.setBrand("Peugeot");
        car.setModel("RXZ");
        car.setRegistrationPlate("22-22-00");
        car.setCustomer(createCustomer());
        return car;
    }
    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Wick");
        customer.setEmail("wick@parabellum.com");
        customer.setAddress("High road 10");
        customer.setPostalCode("33992");
        customer.setCity("New York");

        return customer;
    }
}