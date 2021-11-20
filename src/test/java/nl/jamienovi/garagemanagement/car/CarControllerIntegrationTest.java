package nl.jamienovi.garagemanagement.car;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerIntegrationTest {

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
        Car car = new Car("Tesla","Model S","AA-DD-55");

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
        CarDto car = new CarDto(1,"Ferrari","Berlinetta","11-22-99");
//                new Customer(
//                "Tom",
//                "Cruise",
//                "053-35345435",
//                "tomcruise@hollywood.com",
//                "Hollywood boulevard 131",
//                "90024", "Los Angeles"
//        ));
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
