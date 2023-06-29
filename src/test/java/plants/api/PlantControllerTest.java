package plants.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plants.domain.Genus;
import plants.domain.InvalidPlantException;
import plants.domain.Plant;
import plants.domain.PlantService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static plants.api.PlantControllerAdvice.INVALID_PLANT_DETAIL;

@WebMvcTest(PlantController.class)
class PlantControllerTest {
    MockMvc mockMvc;

    @MockBean
    PlantService mockPlantService;

    @Autowired
    ObjectMapper objectMapper;

    final String PLANT_GET_PATH = "/api/plants";
    final String PLANT_POST_PATH = "/api/plants";
    final String GENUS_GET_PATH = "/api/plants/genera";
    final String GENUS_POST_PATH = "/api/plants/genera";

    @BeforeEach
    void setUp() {
        var plantController = new PlantController(mockPlantService);
        mockMvc = MockMvcBuilders.standaloneSetup(plantController)
                .alwaysDo(MockMvcResultHandlers.print())
                .setControllerAdvice(new PlantControllerAdvice())
                .build();
    }

    @Test
    void getPlants_shouldReturnAllPlants() throws Exception {
        var plants = Instancio.ofList(Plant.class).size(1).create();
        when(mockPlantService.getAllPlants()).thenReturn(plants);

        var expectedBody = objectMapper.writeValueAsString(plants);

        mockMvc.perform(MockMvcRequestBuilders.get(PLANT_GET_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody));
    }

    @Test
    void postPlant_shouldSavePlant() throws Exception {
        var postedPlant = Instancio.of(Plant.class).set(field(Plant::id), null).create();
        var savedPlant = Instancio.of(Plant.class).create();

        when(mockPlantService.savePlant(any(Plant.class))).thenReturn(savedPlant);

        var requestBody = objectMapper.writeValueAsString(postedPlant);
        var expectedResponseBody = objectMapper.writeValueAsString(savedPlant);

        var captor = ArgumentCaptor.forClass(Plant.class);

        mockMvc.perform(post(PLANT_POST_PATH)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        verify(mockPlantService).savePlant(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(postedPlant.id());
        assertThat(captor.getValue().name()).isEqualTo(postedPlant.name());
    }

    @Test
    void postPlant_shouldReturnBadGatewayWhenPlantIsInvalid() throws Exception {
        var invalidPlant = Instancio.of(Plant.class).create();
        var invalidRequestBody = objectMapper.writeValueAsString(invalidPlant);

        when(mockPlantService.savePlant(any(Plant.class))).thenThrow(new InvalidPlantException());

        var errorResponseBody = PlantErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .detail(INVALID_PLANT_DETAIL)
                .type(InvalidPlantException.class.getName())
                .build();

        mockMvc.perform(post(PLANT_POST_PATH)
                        .content(invalidRequestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorResponseBody)));
    }

    @Test
    void getGenera_shouldReturnAllGenera() throws Exception {
        var genera = Instancio.ofList(Genus.class).size(2).create();
        when(mockPlantService.getAllGenera()).thenReturn(genera);

        var expectedBody = objectMapper.writeValueAsString(genera);

        mockMvc.perform(get(GENUS_GET_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody));
    }

    @Test
    void postGenus_shouldSaveGenus() throws Exception {
        var genusToSave = Instancio.of(Genus.class).set(field(Genus::id), null).create();
        var savedGenus = Instancio.of(Genus.class).create();
        when(mockPlantService.saveGenus(any(Genus.class))).thenReturn(savedGenus);

        var captor = ArgumentCaptor.forClass(Genus.class);

        var requestBody = objectMapper.writeValueAsString(genusToSave);
        var expectedResponseBody = objectMapper.writeValueAsString(savedGenus);

        //i care that the controller return the same thing as the service
        mockMvc.perform(post("/api/plants/genera")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));

        //i care that the service was invoked with the correct argument
        verify(mockPlantService).saveGenus(captor.capture());
        assertThat(captor.getValue().id()).isEqualTo(genusToSave.id());
        assertThat(captor.getValue().name()).isEqualTo(genusToSave.name());
    }
}