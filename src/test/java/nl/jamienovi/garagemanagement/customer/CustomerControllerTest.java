package nl.jamienovi.garagemanagement.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jamienovi.garagemanagement.utils.Builder;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Testing weblayer
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerServiceImpl;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(authorities = {"customer:read"})
    void shouldReturnListOfCustomers() throws Exception {
        Customer customer = customerBuilder();

        when(customerServiceImpl.findAll())
                .thenReturn(List.of(customer));

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
        Customer customer = customerBuilder();

        when(customerServiceImpl.findOne(3))
                .thenReturn(customer);

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
        Mockito.when(customerServiceImpl.add(any(Customer.class))).thenReturn(
                customerBuilder());

        Customer customer = Builder.build(Customer.class)
                .with(s -> s.setFirstName("Danni"))
                .with(s -> s.setLastName("Banks"))
                .with(s -> s.setPhoneNumber("054-242342"))
                .with(s -> s.setEmail("danni@instagram.com"))
                .with(s -> s.setAddress("2nd street"))
                .with(s -> s.setPostalCode("93992"))
                .with(s -> s.setCity("Malibu"))
                .get();

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/klanten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customer))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));

        verify(customerServiceImpl).add(any(Customer.class));
    }

    @Test
    @WithMockUser(authorities = {"customer:write"})
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/klanten/{id}",1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerServiceImpl).delete(any(Integer.class));
    }

    private Customer customerBuilder() {
        return Builder.build(Customer.class)
                .with(s -> s.setId(3))
                .with(s -> s.setFirstName("Danni"))
                .with(s -> s.setLastName("Banks"))
                .with(s -> s.setPhoneNumber("054-242342"))
                .with(s -> s.setEmail("danni@instagram.com"))
                .with(s -> s.setAddress("2nd street"))
                .with(s -> s.setPostalCode("93992"))
                .with(s -> s.setCity("Malibu"))
                .get();
    }
}