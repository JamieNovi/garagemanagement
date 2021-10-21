package nl.jamienovi.garagemanagement.car;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void shouldgetCar() {

    }

    @Test
    void getAllCars() throws  Exception{
        Mockito.when(carService.getAllCars()).thenReturn(
                List.of(new Car(1,"Volkswagen","Eos","12-ab-34"))
        );

        RestAssuredMockMvc
                .given()
                .auth().none()
                .when()
                .get("api/auto")
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.equalTo(1));
    }

    @Test
    void addCar() {
    }

    @Test
    void updateCar() {
    }

    @Test
    void deleteCar() {
    }
}