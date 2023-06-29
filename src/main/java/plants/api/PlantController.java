package plants.api;

import org.springframework.web.bind.annotation.*;
import plants.domain.Genus;
import plants.domain.InvalidPlantException;
import plants.domain.Plant;
import plants.domain.PlantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("")
    List<Plant> getPlants() {
        return plantService.getAllPlants();
    }

    @PostMapping("")
    Plant postPlant(@RequestBody Plant plant) throws InvalidPlantException {
        var savedPlant = plantService.savePlant(plant);
        return savedPlant;
    }

    @GetMapping("/genera")
    List<Genus> getGenera() {
        return plantService.getAllGenera();
    }

    @PostMapping("/genera")
    Genus postGenus(@RequestBody Genus genus) {
        return plantService.saveGenus(genus);
    }
}

