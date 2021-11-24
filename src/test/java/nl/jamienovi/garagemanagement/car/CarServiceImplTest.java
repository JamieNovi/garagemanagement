package nl.jamienovi.garagemanagement.car;

import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerServiceImpl;
import nl.jamienovi.garagemanagement.errorhandling.CustomerEntityNotFoundException;
import nl.jamienovi.garagemanagement.utils.Builder;
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
class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerServiceImpl customerServiceImpl;

    @Mock
    private DtoMapper mapper;

    @InjectMocks
    private CarServiceImpl cut;

    @Test
    void shouldGetAllCars() {
        cut.findAll();
        verify(carRepository).findAll();
    }

    @Test
    void shouldGetCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));
        cut.findOne(1);
        verify(carRepository).findById(1);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerEntityNotFoundException.class,() -> cut.findOne(1));
    }

    @Test
    void shouldAddCarToCustomer() {
        Car car = createCar();
        Customer customer = createCustomer();

        when(customerServiceImpl.findOne(1)).thenReturn(customer);
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Integer result = cut.addCarToCustomer(1,car);
        assertEquals(customer.getId(),result);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatedCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerEntityNotFoundException.class,() -> cut.update(1,any()));
    }

    @Test
    void shouldUpdateCarCustomer() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));
        cut.update(1,any());
        verify(carRepository).save(any());
    }

    @Test
    void shouldDeleteCar() {
        when(carRepository.findById(1)).thenReturn(Optional.of(createCar()));
        cut.delete(1);
        verify(carRepository).deleteById(1);
    }
    @Test
    void shouldThrowEntityNotFoundExceptionWhenDeletedCarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerEntityNotFoundException.class,() -> cut.delete(1));
    }

    private CarDto createCarDto() {
        CarDto dto =  Builder.build(CarDto.class)
                .with(s -> s.setId(1))
                .with(s -> s.setBrand("Mini"))
                .with(s -> s.setModel("Cooper"))
                .with(s -> s.setRegistrationPlate("EE-22-22"))
                .get();
        return dto;
    }

    private Car createCar() {
        Car car = Builder.build(Car.class)
                .with(s -> s.setId(1))
                .with(s -> s.setBrand("Peugeot"))
                .with(s -> s.setModel("RXZ"))
                .with(s -> s.setRegistrationPlate("22-22-00"))
                .get();
        return car;
    }
    private Customer createCustomer() {
        Customer customer = Builder.build(Customer.class)
                .with(s -> s.setId(1))
                .with(s -> s.setFirstName("John"))
                .with(s -> s.setLastName("Wick"))
                .with(s -> s.setEmail("wick@parabellum.com"))
                .with(s -> s.setAddress("High road 10"))
                .with(s -> s.setPostalCode("33992"))
                .with(s -> s.setCity("New York"))
                .get();

        return customer;
    }
}