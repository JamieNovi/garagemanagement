package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
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
class CustomerControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getCustomersTest() {
        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .when()
                .get("http://localhost:" + port + "/api/klanten")
                .then()
                .statusCode(200);

    }

    @Test
    void getSingleCustomerTest() {
      RestAssured
              .given()
              .auth().basic("administratie","1234")
              .when()
              .get("http://localhost:" + port + "/api/klanten/2")
              .then()
              .statusCode(200)
              .body("firstName",Matchers.equalTo("Leonardo"))
              .body("lastName",Matchers.equalTo("Dicaprio"))
              .body("city",Matchers.equalTo("Los Angeles"));
    }

    @Test
    void shouldCreateCustomer() throws JsonProcessingException {
        Customer customer = new CustomerBuilder()
//                .setId(4)
                .setFirstName("Jamie")
                .setLastName("Spekman")
                .setPhoneNumber("06-243223")
                .setEmail("jamie@mail.com")
                .setAddress("De Boot 123")
                .setPostalCode("7325 NP")
                .setCity("Apeldoorn").build();
        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
//                .body("{\"firstName\": \"Jamie\", \"lastName\": \"Wallace\", \"phoneNumber\": " +
//                        "\"06-32424\", \"email\": \"jamie@mail.com\", " +
//                        "\"address\": \"Het Schip 148\"," +
//                        "\"postalCode\": \"7325 NP\", \"city\": \"Apeldoorn\"}")
                .body(objectMapper.writeValueAsBytes(customer))
                .when()
                .post("http://localhost:" + port + "/api/klanten")
                .then()
                .statusCode(201)
                .extract();

        RestAssured
                .given()
                .auth().basic("administratie","1234")
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id", Matchers.notNullValue())
                .body("firstName", Matchers.equalTo("Jamie"))
                .body("email", Matchers.equalTo("jamie@mail.com"))
                .body("postalCode", Matchers.equalTo("7325 NP"))
                .body("city", Matchers.equalTo("Apeldoorn"));
    }

    @Test
    void shouldUpdateCustomer() {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .body("{\"id\": \"1\", \"firstName\": \"Tom\", \"lastName\": \"Cruise\", \"phoneNumber\": \"06-43244244\", \"email\": \"tomcruis@hollywood.com\", \"address\": \"Hollywood Boulevard 131\"," +
                        "\"postalCode\": \"90024\", \"city\": \"Garden Grove\"}")
                .when()
                .put("http://localhost:" + port + "/api/klanten/1")
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
                .body("id", Matchers.notNullValue())
                .body("city", Matchers.equalTo("Garden Grove"));

    }
}
