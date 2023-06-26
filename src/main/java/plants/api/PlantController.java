package plants.api;

import org.springframework.web.bind.annotation.*;
import plants.domain.Plant;
import plants.domain.PlantService;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("")
    List<Plant> getAllPlants() {
        return plantService.getAllPlants();
    }

    @PostMapping("")
    Plant savePlant(@RequestBody Plant plant) {
        var savedPlant = plantService.savePlant(plant);
        return savedPlant;
    }
}

