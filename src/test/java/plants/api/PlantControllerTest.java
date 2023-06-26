package plants.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import plants.domain.Plant;
import plants.domain.PlantService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlantController.class)
class PlantControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlantService mockPlantService;

    @Autowired
    ObjectMapper objectMapper;

    final String GET_PATH = "/api/plants";
    final String POST_PATH = "/api/plants";

    @Test
    void getAllPlants_shouldReturnPlants() throws Exception {
        var plants =  Instancio.ofList(Plant.class).size(1).create();
        when(mockPlantService.getAllPlants()).thenReturn(plants);

        var expectedBody = objectMapper.writeValueAsString(plants);

        mockMvc.perform(MockMvcRequestBuilders.get(GET_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody));
    }

    @Test
    void savePlant_shouldSavePlant() throws Exception {
        var postedPlant = Instancio.of(Plant.class).set(field(Plant::id), null).create();
        var savedPlant = Instancio.of(Plant.class).create();

        when(mockPlantService.savePlant(any(Plant.class))).thenReturn(savedPlant);

        var requestBody = objectMapper.writeValueAsString(postedPlant);
        var expectedResponseBody = objectMapper.writeValueAsString(savedPlant);

        var captor = ArgumentCaptor.forClass(Plant.class);

        mockMvc.perform(post(POST_PATH)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        verify(mockPlantService).savePlant(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(postedPlant.id());
        assertThat(captor.getValue().name()).isEqualTo(postedPlant.name());
    }

}