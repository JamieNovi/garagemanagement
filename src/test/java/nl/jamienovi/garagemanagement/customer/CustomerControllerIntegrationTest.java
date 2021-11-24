package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
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
class CustomerControllerIntegrationTest {
    @LocalServerPort
    private Integer port;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldGetCustomersTest() {
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
        Customer customer = Builder.build(Customer.class)
                .with(s -> s.setId(1000))
                .with(s -> s.setFirstName("Jamie"))
                .with(s -> s.setLastName("Spekman"))
                .with(s -> s.setPhoneNumber("06-243223"))
                .with(s -> s.setEmail("jamie@mail.com"))
                .with(s -> s.setAddress("De Boot 123"))
                .with(s -> s.setPostalCode("7325 NP"))
                .with(s -> s.setCity("Apeldoorn"))
                .get();
        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
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
