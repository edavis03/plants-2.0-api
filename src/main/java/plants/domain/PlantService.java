package plants.domain;

import org.springframework.stereotype.Service;
import plants.data.PlantEntity;
import plants.data.PlantRepository;

import java.util.List;

@Service
public class PlantService {
    PlantRepository plantRepo;

    public PlantService(PlantRepository plantRepo) {
        this.plantRepo = plantRepo;
    }

    public List<Plant> getAllPlants() {
        var plantEntities = plantRepo.findAll();
        var plants = plantEntities.stream().map(entity -> {
            return Plant.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build();
        }).toList();

        return plants;
    }
}

