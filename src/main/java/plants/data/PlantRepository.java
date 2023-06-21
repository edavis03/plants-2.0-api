package plants.data;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface PlantRepository extends ListCrudRepository<PlantEntity, UUID> {
}