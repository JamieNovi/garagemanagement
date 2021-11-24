package nl.jamienovi.garagemanagement.car;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nl.jamienovi.garagemanagement.utils.Builder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerIntegrationTest {
    @LocalServerPort
    private Integer port;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetAllCars() {
        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/api/auto")
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.equalTo(3));
    }

    @Test
    void shouldGetSingleCar() {
        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/api/auto/1")
                .then()
                .statusCode(200)
                .body("brand", Matchers.equalTo("Ferrari"))
                .body("model", Matchers.equalTo("California"));;
    }

    @Test
    void shouldCreateCarwithCustomer() throws Exception {
        Car car =  Builder.build(Car.class)
                .with(s -> s.setBrand("Tesla"))
                .with(s -> s.setModel("Model S"))
                .with(s -> s.setRegistrationPlate("AA-DD-55"))
                .get();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .body(objectMapper.writeValueAsBytes(car))
                .when()
                .post("http://localhost:" + port + "/api/auto/1")
                .then()
                .statusCode(201)
                .extract();

        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id",Matchers.notNullValue())
                .body("brand", Matchers.equalTo("Tesla"))
                .body("model",Matchers.equalTo("Model S"))
                .body("registrationPlate",Matchers.equalTo("AA-DD-55"));
    }

    @Test
    void shouldUpdateCar() throws Exception {
        CarDto car =  Builder.build(CarDto.class)
                .with(s -> s.setId(1))
                .with(s -> s.setBrand("Ferrari"))
                .with(s -> s.setModel("Berlinetta"))
                .with(s -> s.setRegistrationPlate("11-22-99"))
                .get();

        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .body(objectMapper.writeValueAsBytes(car))
                .when()
                .put("http://localhost:" + port + "/api/auto/1")
                .then()
                .statusCode(200)
                .extract();

        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("model",Matchers.equalTo("Berlinetta"));
    }
}
