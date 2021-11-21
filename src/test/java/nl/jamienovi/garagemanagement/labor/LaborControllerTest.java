package nl.jamienovi.garagemanagement.labor;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.jamienovi.garagemanagement.errorhandling.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class LaborControllerTestMockMvcStandaloneTest {
    private MockMvc mockmvc;
    private List<Labor> labors = new ArrayList<>();
    private Labor laborDiscBrakes = new Labor();

    @Mock
    private LaborService laborService;

    @InjectMocks
    private LaborController laborController;

    private JacksonTester<Labor> jsonLabor;

    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

        mockmvc = MockMvcBuilders.standaloneSetup(laborController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();

        Labor inspection = new Labor("H0000","Kosten keuring", 50.00);
        laborDiscBrakes = new Labor("HP001","Montagekosten remschijven", 32.50);
        labors.addAll(List.of(inspection,laborDiscBrakes));
    }

    @Test
    void getAll() throws Exception{
        given(laborService.findAll()).willReturn(labors);

        //when
        MockHttpServletResponse response = mockmvc.perform(
                MockMvcRequestBuilders.get("/api/handelingen")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void getLabor() throws Exception {
        given(laborService.findOne(laborDiscBrakes.getId())).willReturn(laborDiscBrakes);

        //when
        MockHttpServletResponse response = mockmvc.perform(
                        MockMvcRequestBuilders.get("/api/handelingen/{id}","HP001")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonLabor.write(new Labor("HP001","Montagekosten remschijven",32.50)).getJson()
        );
    }

    @Test
    void canCreateLabor() throws Exception {
        //when
        MockHttpServletResponse response = mockmvc.perform(
                post("/api/handelingen").contentType(MediaType.APPLICATION_JSON).content(
                        jsonLabor.write(new Labor("HP001","Montagekosten remschijven",32.50)).getJson()
                )).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    void canDeleteLabor() throws Exception {
        //when
        MockHttpServletResponse response = mockmvc.perform(
                delete("/api/handelingen/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }
}