package plants.domain;

import org.springframework.stereotype.Service;
import plants.data.GenusEntity;
import plants.data.GenusRepository;
import plants.data.PlantEntity;
import plants.data.PlantRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PlantService {
    PlantRepository plantRepo;
    GenusRepository genusRepo;

    public PlantService(PlantRepository plantRepo, GenusRepository genusRepo) {
        this.plantRepo = plantRepo;
        this.genusRepo = genusRepo;
    }

    public List<Plant> getAllPlants() {
        var plantEntities = plantRepo.findAll();

        return plantEntities.stream()
                .map(this::convertPlantEntityToPlant)
                .toList();
    }

    public Plant savePlant(Plant plant) throws InvalidPlantException {
        validatePlant(plant);

        var plantEntity = PlantEntity.builder()
                .name(plant.name())
                .build();
        PlantEntity savedEntity = plantRepo.save(plantEntity);
        return convertPlantEntityToPlant(savedEntity);
    }

    private void validatePlant(Plant plant) throws InvalidPlantException {
        if (plant.name() == null) {
            throw new InvalidPlantException();
        }
    }

    private Plant convertPlantEntityToPlant(PlantEntity entity) {
        return Plant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public List<Genus> getAllGenera() {
        var genusEntities = genusRepo.findAll();
        return genusEntities.stream()
                .map(this::convertGenusEntityToGenus)
                .toList();
    }

    private Genus convertGenusEntityToGenus(GenusEntity entity) {
        return Genus.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public Genus saveGenus(Genus genus) {
        var entityToSave = convertGenusToGenusEntity(genus);
        var savedEntity = genusRepo.save(entityToSave);
        return convertGenusEntityToGenus(savedEntity);
    }

    private GenusEntity convertGenusToGenusEntity(Genus genus) {
        return GenusEntity.builder()
                .id(genus.id())
                .name(genus.name())
                .build();
    }
}

