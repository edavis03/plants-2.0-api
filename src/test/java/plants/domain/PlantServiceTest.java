package plants.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import plants.data.PlantEntity;
import plants.data.PlantRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertPlantEntityListEqualsSavedPlantList(plantEntities, returnedPlants);
    }

    private void assertPlantEntityListEqualsSavedPlantList(List<PlantEntity> plantEntities, List<Plant> plants) {
        var plantEntitiesMap = plantEntities.stream().collect(Collectors.toMap(PlantEntity::getId, Function.identity()));
        var plantsMap = plants.stream().collect(Collectors.toMap(Plant::id, Function.identity()));

        for (var entry : plantEntitiesMap.entrySet()) {
            assertPlantEntityEqualsSavedPlant(entry.getValue(), plantsMap.get(entry.getKey()));
        }
    }

    private void assertPlantEntityEqualsSavedPlant(PlantEntity plantEntity, Plant plant) {
        assertEquals(plantEntity.getId(), plant.id());
        assertEquals(plantEntity.getName(), plant.name());
    }
}