package nl.jamienovi.garagemanagement.car;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nl.jamienovi.garagemanagement.customer.Customer;
import nl.jamienovi.garagemanagement.customer.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setup(){
        customer = customerRepository.save(createCustomer());
    };

    @Test
    void shouldGetAllCars() {
        RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/api/auto")
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.equalTo(3));
    }

    @Test
    void shouldGetSingleCar() {

        carService.addCarToCustomer(customer.getId(),createCar());

        RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/api/auto/4")
                .then()
               .statusCode(200)
               .body("brand", Matchers.equalTo("Kia"));

    }

    @Test
    void shouldCreateCarwithCustomer() {

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"brand\": \"Tesla\", \"model\": \"Model S\", " +
                        "\"registrationPlate\": \"11-22-99\"}")
                .when()
                .post("http://localhost:" + port + "/api/auto-toevoegen/4")
                .then()
                .statusCode(201)
                .extract();

        RestAssured
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id",Matchers.notNullValue())
                .body("brand", Matchers.equalTo("Tesla"))
                .body("model",Matchers.equalTo("Model S"))
                .body("registrationPlate",Matchers.equalTo("11-22-99"));
    }

    @Test
    void shouldUpdateCar() {
        Customer customer = customerRepository.save(createCustomer());

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"id\": \"4\", \"brand\": \"Kia\", \"model\": \"Picanto\", " +
                        "\"registrationPlate\": \"00-00-00\"}")
                .when()
                .put("http://localhost:" + port + "/api/auto-aanpassen/4")
                .then()
                .statusCode(200)
                .extract();

        RestAssured
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("registrationPlate",Matchers.equalTo("00-00-00"));

    }

    private Car createCar() {
        Car car = new Car(
                "Kia",
                "Picanto",
                "11-22-33"
        );
        return car;
    }



    private Customer createCustomer(){
        Customer customer = new Customer(
                "Danni",
                "Banks",
                "danni@instagram.com",
                "2nd street",
                "93992",
                "Malibu"

        );
        return customer;
    }

}
