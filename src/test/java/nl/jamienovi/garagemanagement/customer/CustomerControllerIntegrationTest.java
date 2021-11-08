package nl.jamienovi.garagemanagement.customer;

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
    void shouldCreateCustomer() {
        ExtractableResponse<Response> response = RestAssured
                .given()
                .auth().basic("administratie","1234")
                .contentType("application/json")
                .body("{\"firstName\": \"Jamie\", \"lastName\": \"Wallace\", \"email\": \"jamie@mail.com\", " +
                        "\"address\": \"Het Schip 148\"," +
                        "\"postalCode\": \"7325 NP\", \"city\": \"Apeldoorn\"}")
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
                .body("{\"id\": \"1\", \"firstName\": \"Tom\", \"lastName\": \"Cruise\", \"address\": \"Hollywood Boulevard 131\"," +
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
