package nl.jamienovi.garagemanagement.customer;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void getCustomersTest() {
        List<Customer> customers = testRestTemplate.getForObject(
                "http://localhost:" + port + "/api/klanten", List.class);
        Assertions.assertNotNull(customers);
       // assertEquals(4,customers.size());
    }

    @Test
    void getSingleCustomerTest() {
        Customer customer = testRestTemplate.getForObject(
                "http://localhost:" + port + "/api/klanten/1",Customer.class);

        assertEquals(1,customer.getId());
        assertEquals("Tom",customer.getFirstName());
        assertEquals("Cruise", customer.getLastName());
    }

    @Test
    void shouldCreateCustomer() {

        ExtractableResponse<Response> response = RestAssured
                .given()
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
                .when()
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
                .contentType("application/json")
                .body("{\"id\": \"1\", \"firstName\": \"Tom\", \"lastName\": \"Cruise\", \"address\": \"Hollywood Boulevard 131\"," +
                        "\"postalCode\": \"90024\", \"city\": \"Garden Grove\"}")
                .when()
                .put("http://localhost:" + port + "/api/klanten/1")
                .then()
                .statusCode(200)
                .extract();

        RestAssured
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id", Matchers.notNullValue())
                .body("city", Matchers.equalTo("Garden Grove"));

    }


}
