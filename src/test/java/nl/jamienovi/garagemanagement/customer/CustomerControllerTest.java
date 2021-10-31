package nl.jamienovi.garagemanagement.customer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CustomerService customerService;
//
//    private List<Customer> customers;
//
//    @BeforeEach
//    void setUp() {
//        Customer customer1 = new Customer(
//                1,
//                "Famke",
//                "Janssen",
//                "famkejansen@hotmail.com",
//                "Keizersgracht 10",
//                "1002 AB",
//                "Amsterdam"
//        );
//
//        Customer customer2 = new Customer(
//                2,
//                "Gal",
//                "Gadot",
//                "galgodot@hotmail.com",
//                "Kibout 12",
//                "23342ab",
//                "Jeruzalem"
//        );
//        customers = Arrays.asList(customer1,customer2);
//    }
//
//    @Test
//    void shouldGetAllCustomers() throws Exception {
//        //Arrange
//
//        when(customerService.getAllCustomers()).thenReturn(customers);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/klanten")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", is("famkejansen@hotmail.com")));
//    }
//
//    @Test
//    void shouldGetSingleCustomer() throws Exception {
//       Customer customer = new Customer(
//               3,
//               "Danni",
//               "Banks",
//               "danni@instagram.com",
//               "2nd street",
//               "93992",
//               "Malibu"
//
//       );
//
//       when(customerService.getCustomer(3)).thenReturn(customer);
//       mockMvc.perform(MockMvcRequestBuilders.get("/api/klanten/{id}",3)
//               .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldDeleteCustomer() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                .delete("/api/klanten/{id}",1)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//



}