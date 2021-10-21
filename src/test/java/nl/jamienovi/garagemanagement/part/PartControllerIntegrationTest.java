package nl.jamienovi.garagemanagement.part;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nl.jamienovi.garagemanagement.labor.ItemType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PartControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private PartRepository partRepository;

    @BeforeEach
    public void setup(){
        Part part = new Part();
        part.setId("P007");
        part.setName("Brandstof filter");
        part.setPrice(19.95);
        part.setType(ItemType.ONDERDEEL);
        part.setNumberInStock(3);
        partRepository.save(part);
    }

    @Test
    void shouldGetPart() {
         RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("http://localhost:" + port + "/api/onderdelen/P007")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo("P007"))
                .body("name", Matchers.equalTo("Brandstof filter"))
                .body("price", Matchers.equalTo(19.95F))
                .body("type", Matchers.equalTo("ONDERDEEL"))
                .body("numberInStock",Matchers.equalTo(3));

    }


    @Test
    void shouldCreatePart() {

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"id\": \"P006\", \"name\": \"Katalysator\", \"price\": \"160.95\", " +
                        "\"type\": \"onderdeel\", \"numberInStock\": \"2\"}")
                .when()
                .post("http://localhost:" + port + "/api/onderdelen")
                .then()
                .statusCode(201)
                .extract();

        RestAssured
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo("P006"))
                .body("name", Matchers.equalTo("Katalysator"))
                .body("price", Matchers.equalTo(160.95F))
                .body("type", Matchers.equalTo("ONDERDEEL"))
                .body("numberInStock", Matchers.equalTo(2));
    }

    @Test
    void shouldUpdatePart() {

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType("application/json")
                .body("{\"id\": \"P007\", \"name\": \"Brandstoffilter\", \"price\": \"19.95\", " +
                        "\"type\": \"onderdeel\", \"numberInStock\": \"2\"}")
                .when()
                .put("http://localhost:" + port + "/api/onderdelen/P007")
                .then()
                .statusCode(200)
                .extract();

        RestAssured
                .when()
                .get(response.header("Location"))
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo("P007"))
                .body("price", Matchers.equalTo(19.95F))
                .body("numberInStock", Matchers.equalTo(2));
    }
}
