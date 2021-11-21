package nl.jamienovi.garagemanagement.part;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PartController.class)
class PartControllerTest {

    @MockBean
    private PartServiceImpl partServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

    }

    @Test
    @WithMockUser(authorities = {"part:read"})
    void getAllParts() throws Exception {
        Mockito.when(partServiceImpl.findAll()).thenReturn(
                List.of(new Part("P001","Remschijven",49.99,15)));

        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/onderdelen")
                .then()
                .statusCode(200)
                .body("$.size()", Matchers.equalTo(1))
                .body("[0].id",Matchers.equalTo("P001"))
                .body("[0].name",Matchers.equalTo("Remschijven"))
                .body("[0].price",Matchers.equalTo(49.99f))
                .body("[0].numberInStock",Matchers.equalTo(15));

    }

    @Test
    @WithMockUser(authorities = {"part:read"})
    void shouldGetCarPart() throws Exception {
        Mockito.when(partServiceImpl.findOne("P001")).thenReturn(
                new Part("P001","Remschijven",49.99,15));

        RestAssuredMockMvc
                .when()
                .get("/api/onderdelen/P001")
                .then()
                .statusCode(200)
                .body("id",Matchers.equalTo("P001"))
                .body("name",Matchers.equalTo("Remschijven"))
                .body("price",Matchers.equalTo(49.99f))
                .body("numberInStock",Matchers.equalTo(15));
    }

    @Test
    @WithMockUser(authorities = {"part:write"})
    void shouldCreatePart(){
        Part newpart = new Part("P001","katalysator",160.95,2);
        Mockito.when(partServiceImpl.add(any(Part.class))).thenReturn(newpart);

        RestAssuredMockMvc
                .given()
                .contentType("application/json")
                .body("{\"id\": \"P001\", \"name\": \"Katalysator\", \"price\": \"160.95\", " +
                        "\"type\": \"ONDERDEEL\", \"numberInStock\": \"2\"}")
                .when()
                .post("/api/onderdelen")
                .then()
                .statusCode(201)
                .header("Location", Matchers.containsString("/api/onderdelen/P001"));

    }


}