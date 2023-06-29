package plants.domain;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import plants.data.GenusEntity;
import plants.data.GenusRepository;
import plants.data.PlantEntity;
import plants.data.PlantRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {
    @Mock
    PlantRepository mockPlantRepo;

    @Mock
    GenusRepository mockGenusRepo;

    PlantService plantService;

    @BeforeEach
    void setUp() {
        plantService = new PlantService(mockPlantRepo, mockGenusRepo);
    }

    @Test
    void getAllPlants_shouldReturnAllPlants() {
        var plantEntities = getPlantEntityList();
        when(mockPlantRepo.findAll()).thenReturn(plantEntities);

        var returnedPlants = plantService.getAllPlants();

        assertNotNull(returnedPlants);
        assertPlantEntityListEqualsPlantList(plantEntities, returnedPlants);
    }

    @Test
    void savePlant_shouldReturnSavedPlant() throws InvalidPlantException {
        var plantToSave = Instancio.of(Plant.class).set(field(Plant::id), null).create();
        var mockReturnedPlantEntity = Instancio.of(PlantEntity.class).create();

        var captor = ArgumentCaptor.forClass(PlantEntity.class);

        when(mockPlantRepo.save(any(PlantEntity.class))).thenReturn(mockReturnedPlantEntity);

        var savedPlant = plantService.savePlant(plantToSave);

        assertNotNull(savedPlant);
        assertPlantEntityEqualsPlant(mockReturnedPlantEntity, savedPlant);

        verify(mockPlantRepo).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(plantToSave.name());
    }

    @Test
    void savePlant_shouldValidateThatPlantHasName() {
        var invalidPlant = Instancio.of(Plant.class).set(field(Plant::name), null).create();

        assertThrows(InvalidPlantException.class, () -> plantService.savePlant(invalidPlant));
    }

    @Test
    void getAllGenera_shouldReturnAllGenera() {
        var genusEntities = Instancio.ofList(GenusEntity.class).size(2).create();
        when(mockGenusRepo.findAll()).thenReturn(genusEntities);

        var returnedGenera = plantService.getAllGenera();

        assertNotNull(returnedGenera);
        assertGenusEntityListEqualsGenusList(genusEntities, returnedGenera);
    }

    @Test
    void saveGenus_shouldReturnSavedGenus() {
        var genusToSave = Instancio.of(Genus.class).set(field(Genus::id), null).create();
        var savedGenusEntity = Instancio.of(GenusEntity.class).create();
        when(mockGenusRepo.save(any(GenusEntity.class))).thenReturn(savedGenusEntity);
        var captor = ArgumentCaptor.forClass(GenusEntity.class);

        var returnedGenus = plantService.saveGenus(genusToSave);

        assertNotNull(returnedGenus);
        assertThatGenusEqualsGenusEntity(returnedGenus, savedGenusEntity);

        verify(mockGenusRepo).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(genusToSave.id());
        assertThat(captor.getValue().getName()).isEqualTo(genusToSave.name());
    }

    private void assertThatGenusEqualsGenusEntity(Genus domain, GenusEntity entity) {
        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.name(), entity.getName());
    }

    private void assertGenusEntityListEqualsGenusList(List<GenusEntity> genusEntities, List<Genus> returnedGenera) {
        var entityMap = genusEntities.stream().collect(Collectors.toMap(GenusEntity::getId, Function.identity()));
        var domainMap = returnedGenera.stream().collect(Collectors.toMap(Genus::id, Function.identity()));

        for (var entry : entityMap.entrySet()) {
            assertGenusEntityEqualsGenus(entry.getValue(), domainMap.get(entry.getKey()));
        }
    }

    private void assertGenusEntityEqualsGenus(GenusEntity entity, Genus domain) {
        if (entity == null && domain == null) {
            return;
        }
        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getName(), domain.name());
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
        assertGenusEntityEqualsGenus(plantEntity.getGenus(), plant.genus());
    }

    private List<PlantEntity> getPlantEntityList() {
        var plantEntities = List.of(
                Instancio.of(PlantEntity.class).create(),
                Instancio.of(PlantEntity.class).create(),
                Instancio.of((PlantEntity.class))
                        .set(field(PlantEntity::getGenus), null)
                        .create()
        );
        return plantEntities;
    }
}