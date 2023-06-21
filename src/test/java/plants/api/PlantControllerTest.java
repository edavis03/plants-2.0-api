package plants.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import plants.domain.Plant;
import plants.domain.PlantService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(PlantController.class)
class PlantControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlantService mockPlantService;

    @Autowired
    ObjectMapper objectMapper;

    final String GET_PATH = "/api/plants";

    @Test
    void getAllPlants_shouldReturnPlants() throws Exception {
        var plants =  Instancio.ofList(Plant.class).size(1).create();
        when(mockPlantService.getAllPlants()).thenReturn(plants);

        var expectedBody = objectMapper.writeValueAsString(plants);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedBody));
    }
}