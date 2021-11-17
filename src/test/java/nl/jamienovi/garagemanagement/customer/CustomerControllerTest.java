package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Testing weblayer
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = {"customer:read"})
    void shouldReturnListOfCustomers() throws Exception {
        when(customerService.getAllCustomers())
                .thenReturn(List.of(new Customer(3,
                        "Danni",
                        "Banks",
                        "danni@instagram.com",
                        "2nd street",
                        "93992",
                        "Malibu")));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/klanten"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Danni"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("danni@instagram.com"));
    }

    @Test
    @WithMockUser(authorities = {"customer:read"})
    void shouldGetSingleCustomer() throws Exception {
        when(customerService.getCustomer(3))
                .thenReturn(new Customer(3,
                        "Danni",
                        "Banks",
                        "danni@instagram.com",
                        "2nd street",
                        "93992",
                        "Malibu"));

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/klanten/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value("Danni"))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value("danni@instagram.com"));
    }

    @Test
    @WithMockUser(authorities = {"customer:write"})
    void shouldCreateCustomer() throws Exception {
//        Mockito.when(customerService.saveCustomer(any(Customer.class))).thenReturn(1);
        Customer customer = new Customer(
                "Danni",
                "Banks",
                "054-242342",
                "danni@instagram.com",
                "2nd street",
                "93992",
                "Malibu");

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/klanten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customer))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    @WithMockUser(authorities = {"customer:write"})
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/klanten/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomer(any(Integer.class));
    }
}