package plants.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import plants.data.PlantEntity;
import plants.data.PlantRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {
    @Mock
    PlantRepository mockPlantRepo;
    PlantService plantService;

    @BeforeEach
    void setUp() {
        plantService = new PlantService(mockPlantRepo);
    }

    @Test
    void getAllPlants_shouldReturnAllPlants() {
        var plantEntities = Instancio.ofList(PlantEntity.class).size(2).create();
        when(mockPlantRepo.findAll()).thenReturn(plantEntities);

        var returnedPlants = plantService.getAllPlants();

        assertPlantEntityListEqualsPlantList(plantEntities, returnedPlants);
    }

    @Test
    void savePlant_shouldReturnSavedPlant() throws InvalidPlantException {
        var plantToSave = Instancio.of(Plant.class).set(field(Plant::id), null).create();
        var mockReturnedPlantEntity = Instancio.of(PlantEntity.class).create();

        var captor = ArgumentCaptor.forClass(PlantEntity.class);

        when(mockPlantRepo.save(any(PlantEntity.class))).thenReturn(mockReturnedPlantEntity);

        var savedPlant = plantService.savePlant(plantToSave);
        verify(mockPlantRepo).save(captor.capture());

        assertPlantEntityEqualsPlant(mockReturnedPlantEntity, savedPlant);
        assertThat(captor.getValue().getName()).isEqualTo(plantToSave.name());
    }

    @Test
    void savePlant_shouldValidateThatPlantHasName() {
        var invalidPlant = Instancio.of(Plant.class).set(field(Plant::name), null).create();

        assertThrows(InvalidPlantException.class, () -> plantService.savePlant(invalidPlant));
    }

    private void assertPlantEntityListEqualsPlantList(List<PlantEntity> plantEntities, List<Plant> plants) {
        var plantEntitiesMap = plantEntities.stream().collect(Collectors.toMap(PlantEntity::getId, Function.identity()));
        var plantsMap = plants.stream().collect(Collectors.toMap(Plant::id, Function.identity()));

        for (var entry : plantEntitiesMap.entrySet()) {
            assertPlantEntityEqualsPlant(entry.getValue(), plantsMap.get(entry.getKey()));
        }
    }

    private void assertPlantEntityEqualsPlant(PlantEntity plantEntity, Plant plant) {
        assertEquals(plantEntity.getId(), plant.id());
        assertEquals(plantEntity.getName(), plant.name());
    }
}