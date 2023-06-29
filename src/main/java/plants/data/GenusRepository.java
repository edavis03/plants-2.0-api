package plants.data;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface GenusRepository extends ListCrudRepository<GenusEntity, UUID> {
}
