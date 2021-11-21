package nl.jamienovi.garagemanagement.car;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarServiceImpl carServiceImpl;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = {"car:read"})
    void shouldGetAllCars() throws  Exception{
        Mockito.when(carServiceImpl.findAll())
                .thenReturn(List.of(new Car(
                        1,"Tesla","Model 1","KK-9S-SS"
                )));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/auto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].registrationPlate").value("KK-9S-SS"));

    }
    @Test
    @WithMockUser(authorities = {"car:read"})
    void shouldgetCar() throws Exception {
        Mockito.when(carServiceImpl.findOne(1))
                .thenReturn(new Car(
                        1,"Tesla","Model 1","KK-9S-SS"
                ));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/auto/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("brand").value("Tesla"))
                .andExpect(MockMvcResultMatchers.jsonPath("model").value("Model 1"));
    }

    @Test
    @WithMockUser(authorities = {"car:write"})
    void shouldAddCar() throws Exception {
        Car car = new Car(
                "Tesla","Model 1","KK-9S-SS"
        );

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/auto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(car))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

    }

    @Test
    @WithMockUser(authorities = {"car:write"})
    void deleteCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/auto/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carServiceImpl).delete(any(Integer.class));
    }
}